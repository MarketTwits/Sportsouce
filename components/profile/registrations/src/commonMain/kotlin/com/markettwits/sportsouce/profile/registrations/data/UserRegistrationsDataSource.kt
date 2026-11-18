package com.markettwits.sportsouce.profile.registrations.data

import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.registrations.data.cache.UserStartsRegistrationsCache
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Decompose scopes have separate instances and share the same on-disk snapshot.
private val registrationSync = Mutex()

class UserRegistrationsDataSource(
    private val service: SportSauceNetworkProfileApi,
    private val cache: UserStartsRegistrationsCache,
) {
    suspend fun cached(userId: Int): List<UserRegistration>? = cache.forAccount(userId)

    suspend fun refresh(userId: Int, token: String): List<UserRegistration> = registrationSync.withLock {
        val updated = service.userRegistries(userId, token)
        currentCoroutineContext().ensureActive()
        cache.save(userId, updated)
        updated
    }
}
