package com.markettwits.start.register.presentation.registration.data

import com.markettwits.members.member_common.data.ProfileMembersRepository
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.start.register.domain.StartPromo
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegisterPriceMapper
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegisterResultMapper
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegistrationPageMapper
import com.markettwits.start.register.presentation.registration.data.mapper.StartRegistrationPromoMapper
import com.markettwits.start.register.presentation.registration.domain.StartRegistrationRepository
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationDistance
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationPriceResult
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationResult
import com.markettwits.start_cloud.api.register.SportSauceStartRegisterApi
import com.markettwits.start_cloud.model.start.fields.DistinctDistance
import com.markettwits.teams_city.data.TeamsCityRepository
import kotlinx.coroutines.flow.first

class StartRegistrationRepositoryBase(
    private val startRegistrationService: SportSauceStartRegisterApi,
    private val authService: AuthDataSource,
    private val teamsCityRepository: TeamsCityRepository,
    private val profileMembersRepository: ProfileMembersRepository,
    private val priceMapper: StartRegisterPriceMapper,
    private val distancesMapper: StartRegistrationPageMapper,
    private val resultMapper: StartRegisterResultMapper,
    private val promoMapper: StartRegistrationPromoMapper,
) : StartRegistrationRepository {

    override suspend fun isRegisterAvailable(): Result<Unit> = runCatching {
        authService.sharedUser().getOrThrow()
    }

    override suspend fun registerOnStart(
        comboId: Int?,
        startId: Int,
        promo: String,
        registrationWithoutPayment : Boolean,
        distances: List<StartRegistrationDistance>,
    ): Result<StartRegistrationResult>  = runCatching{
        val request = priceMapper.mapPrice(comboId, startId, promo, registrationWithoutPayment, distances)
        val token = authService.updateToken().getOrThrow()
        resultMapper.mapResult(startRegistrationService.register(request, token))
    }

    override suspend fun getStartPrice(
        comboId : Int?,
        startId : Int,
        promo: String,
        distances: List<StartRegistrationDistance>,
    ): Result<StartRegistrationPriceResult> = runCatching {
        val request = priceMapper.mapPrice(comboId, startId, promo, null, distances)
        val token = authService.updateToken().getOrThrow()
        priceMapper.mapPriceResponse(startRegistrationService.price(request, token))
    }

    override suspend fun getStartPromo(
        value: String,
        startId: Int,
        distancesId: List<Int>
    ): Result<StartPromo>  = runCatching {
        promoMapper.map(startRegistrationService.promo(value, startId,distancesId))
    }

    override suspend fun getStartDistances(distances: List<DistinctDistance>): Result<List<StartRegistrationDistance>> =
        runCatching {
            distancesMapper.mapToStartRegistrationDistance(
                user = authService.user().getOrThrow(),
                cities = teamsCityRepository.city().getOrThrow(),
                members = profileMembersRepository.observeMembers(false, true).first(),
                teams = teamsCityRepository.teams().getOrThrow(),
                distinctDistances = distances,
            )
        }
}