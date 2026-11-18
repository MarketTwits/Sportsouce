package com.markettwits.sportsouce.profile.registrations.data.cache

import com.markettwits.cahce.Cache
import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

private const val FILE_NAME = "sportsouce.profile.registrations.account"

private val userStartsRegistrationsCache = storeOfWrapper<CachedUserRegistrations>(
    path = InStorageCacheDirectory.path,
    fileName = FILE_NAME
)

private val legacyRegistrationsCache = storeOfWrapper<List<UserRegistration>>(
    path = InStorageCacheDirectory.path,
    fileName = "sportsouce.profile.registrations",
)

private val diskCache = object : InStorageSingleCache<CachedUserRegistrations>(userStartsRegistrationsCache) {}

class UserStartsRegistrationsCache(
    private val snapshots: Cache<CachedUserRegistrations> = diskCache,
    private val readLegacy: suspend () -> List<UserRegistration>? = { legacyRegistrationsCache.get() },
) {
    suspend fun forAccount(userId: Int): List<UserRegistration>? = try {
        val cached = snapshots.get()
        if (cached == null) {
            readLegacy()?.takeIf { orders -> orders.isNotEmpty() && orders.all { it.userId == userId } }
        } else {
            cached.takeIf { it.userId == userId }?.registrations
        }
    } catch (_: SerializationException) {
        null
    }

    internal suspend fun save(userId: Int, registrations: List<UserRegistration>) {
        snapshots.set(value = CachedUserRegistrations(userId, registrations))
    }
}

@Serializable
data class CachedUserRegistrations(val userId: Int, val registrations: List<UserRegistration>)
