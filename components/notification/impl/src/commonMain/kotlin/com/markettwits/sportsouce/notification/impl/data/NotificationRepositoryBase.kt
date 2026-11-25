package com.markettwits.sportsouce.notification.impl.data

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.api.model.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock

internal class NotificationRepositoryBase(
    private val remote: NotificationRemoteApi,
    private val auth: AuthDataSource,
) : NotificationRepository {
    private val count = MutableStateFlow(0)
    private val reads = MutableStateFlow(NotificationReadState())
    private val changes = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val unreadCount = count.asStateFlow()
    override val readState = reads.asStateFlow()
    override val inboxUpdates = changes.asSharedFlow()
    private val writes = Mutex()
    private val countRefresh = Mutex()
    private var accountId: Int? = null
    private var requestVersion = 0

    override fun clear() = reset(invalidatePages = true)

    override fun invalidateInbox() {
        changes.tryEmit(Unit)
    }

    private fun reset(invalidatePages: Boolean) {
        accountId = null
        requestVersion++
        count.value = 0
        reads.value = NotificationReadState(
            sessionVersion = reads.value.sessionVersion + if (invalidatePages) 1 else 0,
        )
    }

    private suspend fun token(): String {
        val token = auth.updateToken().getOrElse { error ->
            if (error is CancellationException) throw error
            if (error is AuthException) { reset(invalidatePages = false); throw NotificationAuthRequired() }
            throw error
        }
        if (token.isBlank()) { reset(invalidatePages = false); throw NotificationAuthRequired() }
        val id = auth.userID().getOrThrow()
        if (accountId != id) { clear(); accountId = id }
        return token
    }

    private suspend fun <T> authenticated(block: suspend (String) -> T): T {
        val token = token()
        try { return block(token) }
        catch (error: ClientRequestException) {
            if (error.response.status == HttpStatusCode.Unauthorized) {
                reset(invalidatePages = false)
                throw NotificationAuthRequired()
            }
            throw error
        }
    }

    override suspend fun page(offset: Int, limit: Int): NotificationPage = authenticated { token ->
        val version = requestVersion
        val page = remote.page(token, offset, limit)
        currentCoroutineContext().ensureActive()
        if (version != requestVersion) throw NotificationAuthRequired()
        page
    }

    override suspend fun refreshUnreadCount() {
        if (!countRefresh.tryLock()) return
        try {
            writes.withLock {
                authenticated { token ->
                    val version = requestVersion
                    val value = remote.unreadCount(token)
                    currentCoroutineContext().ensureActive()
                    if (version == requestVersion) count.value = value
                }
            }
        } finally {
            countRefresh.unlock()
        }
    }

    override suspend fun read(item: InboxNotification) = writes.withLock {
        authenticated { token ->
            if (!reads.value.apply(item).isRead) {
                val version = requestVersion
                remote.read(token, item.id)
                currentCoroutineContext().ensureActive()
                if (version == requestVersion) {
                    reads.value = reads.value.copy(readIds = reads.value.readIds + item.id)
                    count.value = (count.value - 1).coerceAtLeast(0)
                }
            }
        }
    }

    override suspend fun readAll() = writes.withLock {
        authenticated { token ->
            val version = requestVersion
            val through = Clock.System.now().toString()
            remote.readAll(token)
            currentCoroutineContext().ensureActive()
            if (version == requestVersion) {
                reads.value = reads.value.copy(readThrough = through)
                count.value = 0
            }
        }
    }
}
