package com.markettwits.selfupdater.thirdparty.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.LinkOption
import java.nio.file.StandardCopyOption
import java.nio.file.AtomicMoveNotSupportedException
import java.security.MessageDigest

data class DesktopDownloadRequest(
    val fileName: String,
    val sizeBytes: Long,
    val sha256: String,
)

fun interface DesktopDownloadSource {
    /** The caller owns and closes the returned stream. */
    fun open(): InputStream
}

class DesktopUpdateDownloader internal constructor(
    private val directory: Path,
    private val usableSpace: (Path) -> Long,
) {
    constructor(directory: Path) : this(directory, { Files.getFileStore(it).usableSpace })
    /** Revalidate a previous download against fresh release metadata before offering it again. */
    suspend fun findVerified(request: DesktopDownloadRequest): Path? = withContext(Dispatchers.IO) {
        validate(request)
        val target = directory.resolve(request.fileName)
        if (!Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)) return@withContext null
        try {
            if (Files.size(target) != request.sizeBytes) return@withContext null
            val digest = MessageDigest.getInstance("SHA-256")
            var total = 0L
            Files.newInputStream(target, LinkOption.NOFOLLOW_LINKS).buffered().use { input ->
                val buffer = ByteArray(64 * 1024)
                while (true) {
                    currentCoroutineContext().ensureActive()
                    val count = input.read(buffer)
                    if (count < 0) break
                    total += count
                    if (total > request.sizeBytes) return@withContext null
                    digest.update(buffer, 0, count)
                }
            }
            if (total == request.sizeBytes && digest.hex() == request.sha256) target else null
        } catch (failure: java.io.IOException) {
            null
        }
    }

    /** Download to a temporary file, verify it, and only then expose the finished artifact. */
    suspend fun download(
        request: DesktopDownloadRequest,
        source: DesktopDownloadSource,
        onProgress: (Long, Long) -> Unit = { _, _ -> },
    ): Path = withContext(Dispatchers.IO) {
        validate(request)
        Files.createDirectories(directory)
        check(usableSpace(directory) >= request.sizeBytes) { "Insufficient space for the update" }
        val target = directory.resolve(request.fileName)
        val partial = Files.createTempFile(directory, ".update-", ".part")
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            var total = 0L
            source.open().buffered().use { input ->
                Files.newOutputStream(partial).buffered().use { output ->
                    val buffer = ByteArray(64 * 1024)
                    while (true) {
                        currentCoroutineContext().ensureActive()
                        val count = input.read(buffer)
                        if (count < 0) break
                        if (count == 0) continue
                        total += count
                        check(total <= request.sizeBytes) { "Update exceeds expected size" }
                        digest.update(buffer, 0, count)
                        output.write(buffer, 0, count)
                        onProgress(total, request.sizeBytes)
                    }
                }
            }
            check(total == request.sizeBytes) { "Incomplete update download" }
            val actual = digest.hex()
            check(actual == request.sha256) { "Update checksum mismatch" }
            currentCoroutineContext().ensureActive()
            try {
                Files.move(partial, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING)
            } catch (_: AtomicMoveNotSupportedException) {
                // Keep the operation on the same filesystem even when atomic rename is unsupported.
                Files.move(partial, target, StandardCopyOption.REPLACE_EXISTING)
            }
            target
        } finally {
            Files.deleteIfExists(partial)
        }
    }

    private companion object {
        const val MAX_ASSET_BYTES = 2L * 1024 * 1024 * 1024

        fun validate(request: DesktopDownloadRequest) {
            require(request.fileName.matches(Regex("[A-Za-z0-9][A-Za-z0-9._-]*"))) { "Invalid update filename" }
            require(request.sizeBytes in 1..MAX_ASSET_BYTES) { "Invalid update size" }
            require(request.sha256.length == 64 && request.sha256.all { it in '0'..'9' || it in 'a'..'f' }) { "Invalid update digest" }
        }

        fun MessageDigest.hex() = digest().joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
    }
}
