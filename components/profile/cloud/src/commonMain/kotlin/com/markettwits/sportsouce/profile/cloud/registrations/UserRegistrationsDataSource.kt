package com.markettwits.sportsouce.profile.cloud.registrations

import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

// Decompose scopes have separate instances and share the same on-disk snapshot.
private val registrationSync = Mutex()
private var lastRefresh: Pair<Int, TimeMark>? = null

class UserRegistrationsDataSource(
    private val service: SportSauceNetworkProfileApi,
    private val cache: UserStartsRegistrationsCache,
) {
    suspend fun cached(userId: Int): List<UserRegistration>? = cache.forAccount(userId)

    suspend fun refresh(
        userId: Int,
        token: String,
        maxAge: Duration = Duration.ZERO,
    ): List<UserRegistration> = registrationSync.withLock {
        val previous = lastRefresh
        if (previous?.first == userId && previous.second.elapsedNow() < maxAge) {
            cache.forAccount(userId)?.let { return@withLock it }
        }
        val updated = service.userRegistries(userId, token)
        currentCoroutineContext().ensureActive()
        cache.save(userId, updated)
        lastRefresh = userId to TimeSource.Monotonic.markNow()
        updated
    }
}
