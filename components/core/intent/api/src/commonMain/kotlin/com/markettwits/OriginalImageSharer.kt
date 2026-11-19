package com.markettwits

data class OriginalImageShareProgress(val completed: Int, val total: Int, val bytes: Long = 0)

/** Downloads original bytes before handing any files to the system share sheet. */
interface OriginalImageSharer {
    suspend fun share(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit)

    object Unavailable : OriginalImageSharer {
        override suspend fun share(urls: List<String>, onProgress: (OriginalImageShareProgress) -> Unit): Unit =
            error("Sharing is unavailable")
    }
}
