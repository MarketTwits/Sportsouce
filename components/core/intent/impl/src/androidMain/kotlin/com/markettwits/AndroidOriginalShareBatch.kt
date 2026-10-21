package com.markettwits

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.ContentValues
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

internal class AndroidOriginalShareBatch(private val context: Context) : OriginalShareBatch {
    private val directory = File(context.cacheDir, "images/album-shares/${UUID.randomUUID()}")

    override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile = withContext(Dispatchers.IO) {
        val parent = requireNotNull(directory.parentFile)
        // Keep shared URIs alive after opening the chooser. Clean up only old, completed sessions.
        parent.listFiles()?.filter { System.currentTimeMillis() - it.lastModified() > 24 * 60 * 60 * 1000L }
            ?.forEach { it.deleteRecursively() }
        check(directory.mkdirs() || directory.isDirectory)
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            connectTimeout = 30_000
            readTimeout = 30_000
            setRequestProperty("Accept", "image/*")
            setRequestProperty("Accept-Encoding", "identity")
        }
        val partial = File(directory, "photo-${index + 1}.part")
        val disconnectOnCancel = CoroutineScope(currentCoroutineContext()).launch(Dispatchers.Default, start = CoroutineStart.UNDISPATCHED) {
            try { awaitCancellation() } finally { connection.disconnect() }
        }
        try {
            check(connection.responseCode in 200..299) { "Failed to download original" }
            val mime = connection.contentType.orEmpty().substringBefore(';').lowercase()
            check(mime.startsWith("image/")) { "The response is not an image" }
            val extension = when (mime) {
                "image/jpeg" -> "jpg"; "image/png" -> "png"; "image/webp" -> "webp"
                "image/gif" -> "gif"; "image/avif" -> "avif"; "image/heic" -> "heic"
                else -> "image"
            }
            var received = 0L
            var reportedAt = 0L
            connection.inputStream.use { input ->
                partial.outputStream().use { output ->
                    val buffer = ByteArray(64 * 1024)
                    while (true) {
                        currentCoroutineContext().ensureActive()
                        val count = input.read(buffer)
                        if (count < 0) break
                        output.write(buffer, 0, count)
                        received += count
                        val now = System.nanoTime()
                        if (now - reportedAt > 100_000_000L) { onBytes(received); reportedAt = now }
                    }
                }
            }
            currentCoroutineContext().ensureActive()
            onBytes(received)
            val expected = connection.contentLengthLong
            check(received > 0 && (expected < 0 || received == expected)) { "Incomplete original image" }
            val file = File(directory, "sportsauce-${index + 1}.$extension")
            check(partial.renameTo(file))
            OriginalShareFile(file.absolutePath, mime)
        } finally {
            disconnectOnCancel.cancel()
            connection.disconnect()
            partial.delete()
        }
    }

    override suspend fun launch(files: List<OriginalShareFile>) = withContext(Dispatchers.Main.immediate) {
        val uris = files.map { FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(it.path)) }
        val intent = Intent(if (uris.size == 1) Intent.ACTION_SEND else Intent.ACTION_SEND_MULTIPLE).apply {
            type = files.map { it.mimeType }.distinct().singleOrNull() ?: "image/*"
            if (uris.size == 1) putExtra(Intent.EXTRA_STREAM, uris.first())
            else putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
            clipData = ClipData.newUri(context.contentResolver, "Оригиналы фотографий", uris.first()).apply {
                uris.drop(1).forEach { addItem(ClipData.Item(it)) }
            }
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Поделиться фотографиями").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override suspend fun save(files: List<OriginalShareFile>): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) AndroidAlbumStoragePermissionActivity.request(context)
        return withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val created = mutableListOf<Uri>()
            val legacyFiles = mutableListOf<File>()
            val prefix = "sportsauce-${UUID.randomUUID().toString().take(8)}"
            try {
                files.forEachIndexed { index, photo ->
                    currentCoroutineContext().ensureActive()
                    val source = File(photo.path)
                    val name = "$prefix-${index + 1}.${source.extension}"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val values = ContentValues().apply {
                            put(MediaStore.Images.Media.DISPLAY_NAME, name)
                            put(MediaStore.Images.Media.MIME_TYPE, photo.mimeType)
                            put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Sportsauce")
                            put(MediaStore.Images.Media.IS_PENDING, 1)
                        }
                        val uri = checkNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values))
                        created += uri
                        checkNotNull(resolver.openOutputStream(uri)).use { output ->
                            source.inputStream().use { input -> copyOriginal(input, output) }
                        }
                    } else {
                        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Sportsauce")
                        check(folder.mkdirs() || folder.isDirectory)
                        val target = File(folder, name)
                        legacyFiles += target
                        source.inputStream().use { input -> target.outputStream().use { output -> copyOriginal(input, output) } }
                    }
                }
                currentCoroutineContext().ensureActive()
                // Publish only after the entire batch has been copied. Roll back all entries on error.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    created.forEach { uri ->
                        currentCoroutineContext().ensureActive()
                        check(resolver.update(uri, ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }, null, null) == 1)
                    }
                } else MediaScannerConnection.scanFile(context, legacyFiles.map { it.absolutePath }.toTypedArray(), files.map { it.mimeType }.toTypedArray(), null)
                "Галерея · Sportsauce"
            } catch (e: Throwable) {
                created.forEach { uri -> runCatching { resolver.delete(uri, null, null) } }
                legacyFiles.forEach { it.delete() }
                throw e
            }
        }
    }

    private suspend fun copyOriginal(input: java.io.InputStream, output: java.io.OutputStream) {
        val buffer = ByteArray(64 * 1024)
        while (true) {
            currentCoroutineContext().ensureActive()
            val count = input.read(buffer)
            if (count < 0) break
            output.write(buffer, 0, count)
        }
    }

    override fun discard() { directory.deleteRecursively() }
}
