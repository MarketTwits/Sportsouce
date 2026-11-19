package com.markettwits

import io.ktor.http.Url
import io.ktor.http.decodeURLPart
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

internal data class OriginalShareFile(val path: String, val mimeType: String)

internal interface OriginalShareBatch {
    suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile
    suspend fun launch(files: List<OriginalShareFile>)
    suspend fun save(files: List<OriginalShareFile>): String = error("Saving is unavailable")
    fun discard()
}

/** A batch is atomic: a failed or cancelled download can never share only some selected photos. */
internal class OriginalImageSharerBase(private val newBatch: () -> OriginalShareBatch) : OriginalImageSharer {
    override suspend fun share(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit) {
        prepareOriginalImages(newBatch, urls, onProgress) { batch, files -> batch.launch(files) }
    }
}

internal class OriginalImageDownloaderBase(private val newBatch: () -> OriginalShareBatch) : OriginalImageDownloader {
    override suspend fun download(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit): OriginalImageDownloadResult =
        prepareOriginalImages(newBatch, urls, onProgress) { batch, files ->
            val destination = batch.save(files)
            batch.discard()
            OriginalImageDownloadResult(files.size, destination)
        }
}

private suspend fun <T> prepareOriginalImages(
    newBatch: () -> OriginalShareBatch,
    urls: List<String>,
    onProgress: (OriginalImageShareProgress) -> Unit,
    publish: suspend (OriginalShareBatch, List<OriginalShareFile>) -> T,
): T {
    val originals = urls.map(::originalShareUrl).distinct()
    require(originals.isNotEmpty())
    val batch = newBatch()
    var bytes = 0L
    try {
        onProgress(OriginalImageShareProgress(0, originals.size))
        val files = originals.mapIndexed { index, url ->
            currentCoroutineContext().ensureActive()
            var received = 0L
            val file = batch.download(url, index) { current ->
                received = current
                onProgress(OriginalImageShareProgress(index, originals.size, bytes + current))
            }
            bytes += received
            onProgress(OriginalImageShareProgress(index + 1, originals.size, bytes))
            file
        }
        currentCoroutineContext().ensureActive()
        return publish(batch, files)
    } catch (e: Throwable) {
        batch.discard()
        throw e
    }
}

/** Undo a presentation proxy when present; never share its resized response or a decoded bitmap. */
internal fun originalShareUrl(value: String): String {
    val url = Url(value)
    val original = if (url.host.equals("img.sportsauce.ru", true)) {
        require("/plain/" in url.encodedPath) { "Cannot resolve original photo" }
        url.encodedPath.substringAfter("/plain/").substringBefore('@').decodeURLPart()
    } else value
    val parsed = Url(original)
    require(parsed.protocol.name in setOf("http", "https") && parsed.host.isNotBlank()) { "Invalid photo URL" }
    require(!parsed.host.equals("img.sportsauce.ru", true)) { "Original photo URL is required" }
    return original
}
