package com.markettwits.sportsouce.unauthorized.data

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageFileDirectory
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import kotlinx.serialization.Serializable

@Serializable
internal data class GuestWelcomeCacheEntry(
    val source: String,
    val fetchedAt: Long,
    val config: GuestWelcomeConfig,
    val etag: String? = null,
    val lastModified: String? = null,
)

internal interface GuestWelcomeCache {
    suspend fun read(): GuestWelcomeCacheEntry?
    suspend fun write(entry: GuestWelcomeCacheEntry)
}

internal class GuestWelcomeCacheBase : GuestWelcomeCache {
    private val store = storeOfWrapper<GuestWelcomeCacheEntry>(InStorageFileDirectory.path, "sportsouce.guest-welcome-v1")
    // Migrate previously downloaded JSON without forcing a fresh request after updating the app.
    private val legacy = storeOfWrapper<GuestWelcomeCacheEntry>(InStorageCacheDirectory.path, "sportsouce.guest-welcome-v1")
    override suspend fun read(): GuestWelcomeCacheEntry? = store.get() ?: legacy.get()?.also { store.set(it) }
    override suspend fun write(entry: GuestWelcomeCacheEntry) = store.set(entry)
}
