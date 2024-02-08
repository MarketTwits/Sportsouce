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


    override suspend fun loadOrder(
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
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
            statementMapper.mapOrder(
                cities,
                teams,
                user,
                distanceInfo,
                paymentDisabled,
                paymentType
            )
        }
    }

    override suspend fun promo(value: String, startId: Int): Result<StartPromo> =
        retryRunCatchingAsync {
            promoMapper.map(service.promo(value, startId))
        }

    override suspend fun registry(
        withoutPayment: Boolean,
        statement: OrderStatement,
        distanceInfo: DistanceItem,
        startId: Int
    ): StartRegistryResult {
        val data = runCatching {
            val user = authService.auth()
            val token = authService.updateToken()
            val result = when (distanceInfo) {
                is DistanceItem.DistanceCombo -> {
                    val request = registerMapper.mapNewCombo(
                        withoutPayment = withoutPayment,
                        user = user,
                        startStatement = statement,
                        startDistanceItem = distanceInfo,
                        startId = startId
                    )
                    service.registerOnStartCombo(request, token)
                }
                is DistanceItem.DistanceInfo -> {
                    val request = registerMapper.mapNewBase(
                        withoutPayment = withoutPayment,
                        user = user,
                        startStatement = statement,
                        startDistanceItem = distanceInfo,
                        startId = startId
                    )
                    service.registerOnStartBase(request, token)
                }
            }
            result
        }
        return registrationResponseMapper.flatMap(data)
    }
}