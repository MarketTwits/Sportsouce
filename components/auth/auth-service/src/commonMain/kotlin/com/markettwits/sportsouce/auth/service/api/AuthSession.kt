package com.markettwits.sportsouce.auth.service.api

import com.markettwits.sportsouce.auth.service.internal.database.data.store.AuthCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/** Local credentials, available even when the device is offline. */
class AuthSession(
    val userId: Int,
    val accessToken: String,
)

interface CachedAuthSession {
    val userIds: Flow<Int?>
    suspend fun current(): AuthSession?
}

internal class CachedAuthSessionBase(private val cache: AuthCacheDataSource) : CachedAuthSession {
    override val userIds = cache.observe().map { it?.userId?.toInt() }.distinctUntilChanged()

    override suspend fun current(): AuthSession? = cache.current()?.let {
        AuthSession(it.userId.toInt(), it.accessToken)
    }
}

/** Optional application hook for removing account bindings before clearing credentials. */
fun interface AuthSessionEnding {
    suspend fun beforeClear(session: AuthSession)
}
