package com.markettwits.start.data.registration

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.start.domain.StartStatement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class RegistrationStartRepositoryBase(
    private val service : SportsouceApi,
    private val authService : AuthDataSource,
    private val statementMapper: RegistrationRemoteToDomainMapper
) : RegistrationStartRepository {
    override suspend fun preload(): Result<StartStatement> {
        val result = runCatching {
            val (teams, cities, user) = coroutineScope {
                withContext(Dispatchers.IO) {
                    authService.updateToken()
                    val deferredWithTeams = async { service.teams() }
                    val deferredWithCities = async { service.cities() }
                    val deferredWithUser = async { authService.auth() }
                    Triple(
                        deferredWithTeams.await(),
                        deferredWithCities.await(),
                        deferredWithUser.await(),
                    )
                }
            }
            statementMapper.map(cities,teams,user)
        }
       return result
    }
}