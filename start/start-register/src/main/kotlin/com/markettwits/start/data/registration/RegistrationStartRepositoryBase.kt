package com.markettwits.start.data.registration

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync
import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.start.data.registration.mapper.RegistrationMapper
import com.markettwits.start.data.registration.mapper.RegistrationPromoMapper
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapper
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.domain.StartPromo
import com.markettwits.start.domain.StartRegistryResult
import com.markettwits.start.presentation.order.domain.OrderStatement
import com.markettwits.teams_city.data.TeamsCityRepository
import kotlinx.coroutines.flow.first


class RegistrationStartRepositoryBase(
    private val service: SportsouceApi,
    private val authService: AuthDataSource,
    private val statementMapper: RegistrationRemoteToDomainMapper,
    private val registerMapper: RegistrationMapper,
    private val teamsCityRepository: TeamsCityRepository,
    private val profileMembersRepository: ProfileMembersRepository,
    private val registrationResponseMapper: RegistrationResponseMapper,
    private val promoMapper: RegistrationPromoMapper,
) : RegistrationStartRepository {

    override suspend fun loadOrder(
        startTitle: String,
        distanceInfo: DistanceItem,
        paymentDisabled: Boolean,
        paymentType: String
    ): Result<OrderStatement> {
        return retryRunCatchingAsync {
            val user = authService.user().getOrThrow()
            val cities = teamsCityRepository.city().getOrThrow()
            val teams = teamsCityRepository.teams().getOrThrow()
            val members = profileMembersRepository.observeMembers(false, true).first()
            statementMapper.mapOrder(
                startTitle,
                cities,
                teams,
                members,
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
                        user = user.getOrThrow(),
                        startStatement = statement,
                        startDistanceItem = distanceInfo,
                        startId = startId
                    )
                    service.registerOnStartCombo(request, token.getOrThrow())
                }

                is DistanceItem.DistanceInfo -> {
                    val request = registerMapper.mapNewBase(
                        withoutPayment = withoutPayment,
                        user = user.getOrThrow(),
                        startStatement = statement,
                        startDistanceItem = distanceInfo,
                        startId = startId
                    )
                    service.registerOnStartBase(request, token.getOrThrow())
                }
            }
            result
        }
        return registrationResponseMapper.flatMap(data)
    }
}