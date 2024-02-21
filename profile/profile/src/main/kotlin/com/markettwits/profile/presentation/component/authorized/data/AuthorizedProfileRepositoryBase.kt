package com.markettwits.profile.presentation.component.authorized.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.presentation.component.authorized.data.mapper.AuthorizedProfileMapper
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AuthorizedProfileRepositoryBase(
    private val auth: AuthDataSource,
    private val remote: SportsouceApi,
    private val mapper: AuthorizedProfileMapper
) : AuthorizedProfileRepository {
    override suspend fun profile(): Result<UserProfile> =
        runCatching {
            val user = auth.auth()
            val token = auth.updateToken()
            val (members, registers) = coroutineScope {
                val deferredMembers = async { remote.memberTemplate(user.id, token) }
                val deferredRegisters = async {
                    remote.userRegistries(user.id, token)
                }
                Pair(deferredMembers.await(), deferredRegisters.await())
            }
            mapper.map(user, registers, members)
        }
}