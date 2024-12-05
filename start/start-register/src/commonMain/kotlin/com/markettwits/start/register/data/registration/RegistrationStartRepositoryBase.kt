package com.markettwits.start.register.data.registration

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.start.register.data.registration.mapper.RegistrationPromoMapper
import com.markettwits.start.register.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.register.domain.StartPromo
import com.markettwits.teams_city.data.TeamsCityRepository


class RegistrationStartRepositoryBase(
    private val service: SportsouceApi,
    private val authService: AuthDataSource,
    private val teamsCityRepository: TeamsCityRepository,
    private val profileMembersRepository: ProfileMembersRepository,
    private val registrationResponseMapper: RegistrationResponseMapper,
    private val promoMapper: RegistrationPromoMapper,
) : RegistrationStartRepository {


    override suspend fun promo(value: String, startId: Int,distancesId : List<Int>): Result<StartPromo> =
        runCatching {
            promoMapper.map(service.promo(value, startId,distancesId))
        }

}