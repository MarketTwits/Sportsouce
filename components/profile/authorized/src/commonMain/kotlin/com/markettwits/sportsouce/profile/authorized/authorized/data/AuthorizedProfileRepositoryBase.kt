package com.markettwits.sportsouce.profile.authorized.authorized.data

import com.markettwits.cahce.Cache
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.authorized.authorized.data.mapper.AuthorizedProfileMapper
import com.markettwits.sportsouce.profile.authorized.authorized.domain.AuthorizedProfileRepository
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException

class AuthorizedProfileRepositoryBase(
    private val auth: AuthDataSource,
    private val remote: SportSauceNetworkProfileApi,
    private val cache: Cache<UserProfile>,
    private val mapper: AuthorizedProfileMapper,
) : AuthorizedProfileRepository {

    override suspend fun profile(forced: Boolean): Flow<UserProfile> = flow {
        val userId = auth.userID().getOrThrow()
        val cached = readCache()?.takeIf { it.id == userId }
        if (!forced && cached != null) emit(cached)

        // Commit a complete successful snapshot. A failed registrations request is
        // not an empty list and must never replace the user's registrations in cache.
        val updated = loadProfile()
        cache.set(value = updated)
        emit(updated)
    }

    private suspend fun readCache(): UserProfile? = try {
        cache.get()
    } catch (_: SerializationException) {
        cache.clear()
        null
    }

    private suspend fun loadProfile(): UserProfile = coroutineScope {
        val user = auth.auth().getOrThrow()
        val token = auth.updateToken().getOrThrow()
        val members = async { remote.memberTemplate(user.id, token).rows }
        val registrations = async { remote.userRegistries(user.id, token) }
        mapper.map(user, registrations.await(), members.await())
    }
}
