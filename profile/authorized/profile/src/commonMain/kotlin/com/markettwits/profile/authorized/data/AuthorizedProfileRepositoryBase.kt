package com.markettwits.profile.authorized.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.authorized.data.cache.UserProfileCache
import com.markettwits.profile.authorized.data.mapper.AuthorizedProfileMapper
import com.markettwits.profile.authorized.domain.AuthorizedProfileRepository
import com.markettwits.profile.authorized.domain.UserProfile
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class AuthorizedProfileRepositoryBase(
    private val auth: AuthDataSource,
    private val remote: SportsouceApi,
    private val cache: UserProfileCache,
    private val executeWithCache: ExecuteWithCache,
    private val mapper: AuthorizedProfileMapper
) : AuthorizedProfileRepository {
    override suspend fun profile(forced: Boolean): Flow<UserProfile> =
        auth.observeUser().flatMapConcat {
            flow {
                executeWithCache.executeWithCache(
                    forced = forced,
                    cache = cache,
                    launch = ::launch,
                    callback = { user ->
                        emit(user)
                    }
                )
            }
        }

    private suspend fun launch(): UserProfile {
        val (user, members, registers) = coroutineScope {
            val user = auth.auth().getOrThrow()
            val token = auth.updateToken().getOrThrow()
            val deferredMembers = async {
                runCatching {
                    remote.memberTemplate(user.id, token).rows
                }.fold(onSuccess = {
                    it
                }, onFailure = {
                    emptyList()
                })

            }
            val deferredRegisters = async {
                runCatching { remote.userRegistries(user.id, token) }.fold(onSuccess = {
                    it
                }, onFailure = {
                    emptyList()
                })
            }
            Triple(user, deferredMembers.await(), deferredRegisters.await())
        }
        return mapper.map(user, registers, members)
    }
}