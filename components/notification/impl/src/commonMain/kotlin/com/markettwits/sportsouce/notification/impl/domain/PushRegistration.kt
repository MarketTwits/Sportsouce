package com.markettwits.sportsouce.notification.impl.domain

import com.markettwits.core.log.TaggedLogger
import com.markettwits.core.log.errorLog
import com.markettwits.sportsouce.auth.service.api.AuthSession
import com.markettwits.sportsouce.auth.service.api.CachedAuthSession
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.io.IOException
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

internal interface PushRegistrationPlatform {
    fun isSupported(): Boolean
    fun canShowNotifications(): Boolean
    fun setAutoInit(enabled: Boolean)
    suspend fun token(): String
    suspend fun installationId(create: Boolean): String?
    fun registeredAt(installationId: String, userId: Int, token: String): Instant?
    fun saveRegistration(installationId: String, userId: Int, token: String, registeredAt: Instant)
    fun clearRegistration()
    fun dismissAccountNotifications()
}

internal interface PushDeviceRepository {
    suspend fun register(installationId: String, accessToken: String, fcmToken: String)
    suspend fun unregister(installationId: String, accessToken: String)
}

/** Owns the push binding for the current account. The scope and callers run on Main. */
internal class PushRegistration(
    scope: CoroutineScope,
    private val sessions: CachedAuthSession,
    private val renewAccessToken: suspend () -> String,
    private val devices: PushDeviceRepository,
    private val platform: PushRegistrationPlatform,
    private val clock: Clock = Clock.System,
) {
    private val logger = TaggedLogger("SportsaucePush")
    private val requests = Channel<Unit>(Channel.CONFLATED)
    private var syncJob: Job? = null
    private var signingOut: Int? = null
    private var disabledAccount: Int? = null

    val userIds = sessions.userIds.retry(1) { it !is CancellationException }
        .catch { error ->
            if (error is CancellationException) throw error
            logFailure(error)
            emit(null)
        }
        .stateIn(scope, SharingStarted.Eagerly, null)

    init {
        scope.launch {
            var previousUser: Int? = null
            userIds.collect { userId ->
                if (previousUser != userId) {
                    if (previousUser != null) platform.dismissAccountNotifications()
                    if (signingOut != userId) signingOut = null
                    syncJob?.cancel()
                    disabledAccount = null
                    previousUser = userId
                }
                refresh()
            }
        }
        scope.launch {
            for (request in requests) {
                val job = launch { synchronizeWithRetry() }
                syncJob = job
                job.join()
                syncJob = null
            }
        }
    }

    fun refresh() {
        requests.trySend(Unit)
    }

    fun deliverySettingsChanged() {
        syncJob?.cancel()
        refresh()
    }

    suspend fun isCurrentAccount(userId: Int): Boolean = try {
        sessions.current()?.userId == userId && signingOut != userId
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (error: Exception) {
        logFailure(error)
        false
    }

    suspend fun beforeSignOut(session: AuthSession) {
        signingOut = session.userId
        platform.dismissAccountNotifications()
        syncJob?.cancelAndJoin()
        platform.clearRegistration()
        disabledAccount = null
        try {
            if (platform.isSupported()) platform.setAutoInit(false)
            withTimeoutOrNull(1_500) {
                platform.installationId(create = false)?.let { devices.unregister(it, session.accessToken) }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            logFailure(error)
        }
    }

    private suspend fun synchronizeWithRetry() {
        for (delayMillis in listOf(0L, 2_000L, 8_000L)) {
            delay(delayMillis)
            try {
                val completed = withTimeoutOrNull(15_000) {
                    synchronize()
                    true
                }
                if (completed == true) return
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                logFailure(error)
                val status = (error as? ResponseException)?.response?.status?.value
                if (error !is IOException && error !is HttpRequestTimeoutException &&
                    status != 408 && status != 429 && (status == null || status !in 500..599)
                ) return
            }
        }
    }

    private suspend fun synchronize() {
        if (!platform.isSupported()) return
        val session = sessions.current()
        if (session == null || signingOut == session.userId) {
            platform.setAutoInit(false)
            return
        }

        val enabled = platform.canShowNotifications()
        platform.setAutoInit(enabled)
        if (enabled) register(session) else unregister(session)
    }

    private suspend fun register(session: AuthSession) {
        disabledAccount = null
        val token = platform.token()
        if (!isCurrentAccount(session.userId) || !platform.canShowNotifications()) return
        val installation = requireNotNull(platform.installationId(create = true))
        val previous = platform.registeredAt(installation, session.userId, token)
        if (previous != null && clock.now() - previous in Duration.ZERO..<7.days) return
        if (withAccessToken(session) { devices.register(installation, it, token) }) {
            platform.saveRegistration(installation, session.userId, token, clock.now())
        }
    }

    private suspend fun unregister(session: AuthSession) {
        if (disabledAccount == session.userId) return
        platform.clearRegistration()
        platform.dismissAccountNotifications()
        val installation = platform.installationId(create = false)
        if (installation != null && !withAccessToken(session) { devices.unregister(installation, it) }) return
        disabledAccount = session.userId
    }

    private suspend fun withAccessToken(session: AuthSession, request: suspend (String) -> Unit): Boolean {
        if (!isCurrentAccount(session.userId)) return false
        try {
            request(session.accessToken)
        } catch (error: ClientRequestException) {
            if (error.response.status != HttpStatusCode.Unauthorized || !isCurrentAccount(session.userId)) throw error
            val renewedToken = renewAccessToken()
            if (!isCurrentAccount(session.userId)) return false
            request(renewedToken)
        }
        return true
    }

    private fun logFailure(error: Throwable) {
        val reason = (error as? ResponseException)?.response?.status?.value ?: error::class.simpleName
        logger.errorLog { "Push operation failed ($reason)" }
    }

}
