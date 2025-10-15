package com.markettwits.sportsouce.club.common.data

import com.markettwits.core_ui.items.extensions.fetchFifth
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.club.cloud.api.SportSauceClubsNetworkApi
import com.markettwits.sportsouce.club.common.data.mapper.club_info.ClubInfoMapper
import com.markettwits.sportsouce.club.common.data.mapper.subscription.SubscriptionMapper
import com.markettwits.sportsouce.club.common.domain.ClubRepository
import com.markettwits.sportsouce.club.dashboard.domain.SubscriptionItems
import com.markettwits.sportsouce.club.info.domain.models.*
import com.markettwits.sportsouce.club.registration.domain.WorkoutPrice
import com.markettwits.sportsouce.club.registration.domain.WorkoutPriceForm
import com.markettwits.sportsouce.club.registration.domain.WorkoutRegistrationForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClubRepositoryBase(
    private val clubService: SportSauceClubsNetworkApi,
    private val subscriptionMapper: SubscriptionMapper,
    private val clubInfoMapper: ClubInfoMapper,
    private val authDataSource: AuthDataSource,
) : ClubRepository {

    override suspend fun schedule(workoutId: Int?): Result<List<Schedule>> = runCatching {
        clubInfoMapper.mapSchedule(clubService.schedule(workoutId))
    }

    override suspend fun subscriptions(): Flow<List<SubscriptionItems>> = flow {
        val subscriptions = subscriptionMapper.map(clubService.subscription())
        emit(subscriptions)
    }

    override suspend fun workoutRegistrationUserData(): WorkoutRegistrationForm =
        subscriptionMapper.map(authDataSource.sharedUser())

    override suspend fun workoutRegistration(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit> =
        runCatching {
            clubService.workoutRequest(subscriptionMapper.map(workoutRegistrationForm))
        }

    override suspend fun workoutRegistrationPrice(
        workoutPriceForm: WorkoutPriceForm
    ): Result<WorkoutPrice> = runCatching {
        val result = clubService.workoutRequestPrice(subscriptionMapper.map(workoutPriceForm))
        subscriptionMapper.map(result)
    }

    override suspend fun clubInfo(): Result<List<ClubInfo>> = runCatching {
        val clubSettings = clubService.clubSettings()
        val cloud =
            fetchFifth<List<Trainer>, List<Question>, List<Statistic>, List<Training>, List<Schedule>>(
                { clubInfoMapper.mapTrainers(clubService.trainers()) },
                { clubInfoMapper.mapQuestions(clubService.questions()) },
                { clubInfoMapper.mapStatistics(clubSettings) },
                { clubInfoMapper.mapTraining(clubService.workout()) },
                { clubInfoMapper.mapSchedule(clubService.schedule()) }
            )

        val result = mutableListOf<ClubInfo>()

        clubInfoMapper.mapMainImage(clubSettings)?.let { mainImage ->
            result.add(ClubInfo.MainImage(mainImage))
        }

        val features = clubInfoMapper.mapFeatures(clubSettings)
        if (features.isNotEmpty()) {
            result.add(ClubInfo.Features(features))
        }

        result.addAll(
            listOf(
            ClubInfo.Commands(cloud.first),
            ClubInfo.Questions(cloud.second),
            ClubInfo.Statistics(cloud.third),
            ClubInfo.Trainings(cloud.fourth),
            ClubInfo.Schedules(cloud.fifth)
            )
        )

        result
    }


}