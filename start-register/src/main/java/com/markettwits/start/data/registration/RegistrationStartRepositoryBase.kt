package com.markettwits.start.data.registration

import android.util.Log
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.start.data.registration.mapper.RegistrationMapper
import com.markettwits.start.data.registration.mapper.RegistrationPromoMapper
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapper
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.domain.StartPromo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.domain.StartStatement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


class RegistrationStartRepositoryBase(
    private val service: SportsouceApi,
    private val authService: AuthDataSource,
    private val statementMapper: RegistrationRemoteToDomainMapper,
    private val registerMapper: RegistrationMapper,
    private val registrationResponseMapper: RegistrationResponseMapper,
    private val promoMapper: RegistrationPromoMapper,
) : RegistrationStartRepository {
    override suspend fun preload(price: String, paymentDisabled : Boolean): Result<StartStatement> =
        retryRunCatchingAsync {
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
            statementMapper.map(cities, teams, user, price,paymentDisabled)
        }

    override suspend fun promocode(value: String, startId: Int): Result<StartPromo> =
        retryRunCatchingAsync {
            promoMapper.map(service.promo(value, startId))
        }

    override suspend fun save(
        statement: StartStatement,
        distanceInfo: DistanceInfo,
        startId: Int
    ): Result<StartRegistryResult> {
        return retryRunCatchingAsync{
            val user = authService.auth()
            val token = authService.updateToken()
            val request = registerMapper.map(
                withoutPayment = true,
                user = user,
                startStatement = statement,
                startDistanceInfo = distanceInfo,
                startId = startId
            )
            val result = retryRunCatchingAsync {
                service.registerOnStartWithoutPayment(request, token)
            }
            registrationResponseMapper.flatMapWithoutPayment(result)
        }
    }

    override suspend fun pay(
        statement: StartStatement,
        distanceInfo: DistanceInfo,
        startId: Int
    ): Result<StartRegistryResult> {
        return retryRunCatchingAsync{
            val user = authService.auth()
            val token = authService.updateToken()
            val request = registerMapper.map(
                withoutPayment = false,
                user = user,
                startStatement = statement,
                startDistanceInfo = distanceInfo,
                startId = startId
            )
            val result = retryRunCatchingAsync {
                service.registerOnStart(request, token)
            }
            registrationResponseMapper.flatMap(result)
        }
    }
}

inline fun <R, T> Result<R>.flatMap(transform: (R) -> Result<T>) {
    fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )
}