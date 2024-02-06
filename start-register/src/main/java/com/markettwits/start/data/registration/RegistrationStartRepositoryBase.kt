package com.markettwits.start.data.registration

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.start.data.registration.mapper.RegistrationMapper
import com.markettwits.start.data.registration.mapper.RegistrationPromoMapper
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapper
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.domain.StartPromo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement
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
    override suspend fun preload(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean
    ): Result<StartStatement> =
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
            statementMapper.map(cities, teams, user, distanceInfo, paymentDisabled)
        }

    override suspend fun loadOrder(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean
    ): Result<OrderStatement> {
        return retryRunCatchingAsync {
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
            statementMapper.mapOrder(cities, teams, user, distanceInfo, paymentDisabled)
        }
    }

    override suspend fun promo(value: String, startId: Int): Result<StartPromo> =
        retryRunCatchingAsync {
            promoMapper.map(service.promo(value, startId))
        }

    override suspend fun registry(
        withoutPayment: Boolean,
        statement: StartStatement,
        distanceInfo: DistanceItem,
        startId: Int
    ): Result<StartRegistryResult> {
        return retryRunCatchingAsync {
            val user = authService.auth()
            val token = authService.updateToken()
            val result = when (distanceInfo) {
                is DistanceItem.DistanceCombo -> {
                    val request = registerMapper.mapCombo(
                        withoutPayment = withoutPayment,
                        user = user,
                        startStatement = statement,
                        startDistanceItem = distanceInfo,
                        startId = startId
                    )
                    val result = retryRunCatchingAsync {
                        service.registerOnStartCombo(request, token)
                    }
                    registrationResponseMapper.flatMap(result)
                }

                is DistanceItem.DistanceInfo -> {
                    val request = registerMapper.mapBase(
                        withoutPayment = withoutPayment,
                        user = user,
                        startStatement = statement,
                        startDistanceItem = distanceInfo,
                        startId = startId
                    )
                    val result = retryRunCatchingAsync {
                        service.registerOnStartBase(request, token)
                    }
                    registrationResponseMapper.flatMap(result)
                }
            }
            result
        }
    }
}

inline fun <R, T> Result<R>.flatMap(transform: (R) -> Result<T>) {
    fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )
}