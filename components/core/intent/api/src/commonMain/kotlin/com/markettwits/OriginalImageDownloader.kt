package com.markettwits

data class OriginalImageDownloadResult(val count: Int, val destination: String)

/** Saves the source files, independently of the resized images used by the gallery. */
interface OriginalImageDownloader {
    suspend fun download(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit): OriginalImageDownloadResult

    object Unavailable : OriginalImageDownloader {
        override suspend fun download(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit): OriginalImageDownloadResult =
            error("Downloading is unavailable")
    }
}
