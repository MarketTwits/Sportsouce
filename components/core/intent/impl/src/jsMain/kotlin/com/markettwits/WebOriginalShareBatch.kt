package com.markettwits

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import kotlin.js.Promise

internal class WebOriginalShareBatch : OriginalShareBatch {
    private val blobs = mutableListOf<Blob>()
    override suspend fun download(url: String, index: Int, onBytes: (Long) -> Unit): OriginalShareFile {
        val response = window.fetch(url).await()
        check(response.ok)
        val blob = response.blob().await()
        check(blob.type.startsWith("image/") && blob.size.toDouble() > 0)
        blobs += blob
        onBytes(blob.size.toDouble().toLong())
        return OriginalShareFile(index.toString(), blob.type)
    }
    override suspend fun launch(files: List<OriginalShareFile>) {
        val nativeFiles = nativeFiles(files)
        val navigator = window.navigator.asDynamic()
        val payload = js("({})"); payload.files = nativeFiles
        if (navigator.canShare != undefined && navigator.canShare(payload) == true) {
            try {
                (navigator.share(payload) as Promise<dynamic>).await()
                return
            } catch (e: Throwable) {
                if (e.asDynamic().name == "AbortError") return
                // Some browsers require a fresh user activation after downloading; export originals.
            }
        }
        save(files)
    }
    private fun nativeFiles(files: List<OriginalShareFile>): Array<dynamic> = files.map { file ->
            val blob = blobs[file.path.toInt()]
            val extension = if (blob.type == "image/jpeg") "jpg" else blob.type.substringAfter('/').substringBefore('+')
            val name = "sportsauce-${file.path.toInt() + 1}.$extension"
            val parts = arrayOf(blob)
            val options = js("({})"); options.type = blob.type
            js("new File(parts, name, options)")
        }.toTypedArray()
    override suspend fun save(files: List<OriginalShareFile>): String {
        nativeFiles(files).forEach { file ->
            val url = URL.createObjectURL(file as Blob)
            val anchor = (document.createElement("a") as HTMLAnchorElement).apply { href = url; download = file.name as String }
            document.body?.appendChild(anchor); anchor.click(); anchor.remove()
            window.setTimeout({ URL.revokeObjectURL(url) }, 60_000)
        }
        return "Загрузки браузера"
    }
    override fun discard() { blobs.clear() }
}
