package com.markettwits

import kotlinx.coroutines.*
import java.awt.Desktop
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files

internal class DesktopOriginalShareBatch : OriginalShareBatch {
    private val directory = Files.createTempDirectory("sportsauce-originals-").toFile()
    override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile = withContext(Dispatchers.IO) {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply { connectTimeout = 30_000; readTimeout = 30_000 }
        val partial = File(directory, "photo-$index.part")
        try {
            check(connection.responseCode in 200..299)
            val mime = connection.contentType.orEmpty().substringBefore(';')
            check(mime.startsWith("image/"))
            var bytes = 0L
            connection.inputStream.use { input -> partial.outputStream().use { output ->
                val buffer = ByteArray(64 * 1024)
                while (true) {
                    currentCoroutineContext().ensureActive()
                    val count = input.read(buffer)
                    if (count < 0) break
                    output.write(buffer, 0, count); bytes += count; onBytes(bytes)
                }
            } }
            check(bytes > 0 && (connection.contentLengthLong < 0 || bytes == connection.contentLengthLong))
            val extension = when (mime) { "image/jpeg" -> "jpg"; "image/png" -> "png"; "image/webp" -> "webp"; else -> "image" }
            val file = File(directory, "sportsauce-${index + 1}.$extension")
            check(partial.renameTo(file))
            OriginalShareFile(file.absolutePath, mime)
        } finally { connection.disconnect(); partial.delete() }
    }
    override suspend fun launch(files: List<OriginalShareFile>) = withContext(Dispatchers.IO) {
        check(Desktop.isDesktopSupported())
        Desktop.getDesktop().open(directory)
    }
    override suspend fun save(files: List<OriginalShareFile>): String = withContext(Dispatchers.IO) {
        val parent = File(System.getProperty("user.home"), "Downloads/Sportsauce")
        check(parent.mkdirs() || parent.isDirectory)
        val target = Files.createTempDirectory(parent.toPath(), "album-").toFile()
        try {
            files.forEach { photo ->
                currentCoroutineContext().ensureActive()
                val source = File(photo.path)
                source.copyTo(File(target, source.name))
            }
            currentCoroutineContext().ensureActive()
            target.absolutePath
        } catch (e: Throwable) { target.deleteRecursively(); throw e }
    }
    override fun discard() { directory.deleteRecursively() }
}
