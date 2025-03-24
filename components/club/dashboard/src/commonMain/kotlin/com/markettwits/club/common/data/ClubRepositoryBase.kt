package com.markettwits.club.common.data

import com.markettwits.club.cloud.api.SportSauceClubsApi
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.common.data.mapper.club_info.ClubInfoMapper
import com.markettwits.club.common.data.mapper.subscription.SubscriptionMapper
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.domain.models.Question
import com.markettwits.club.info.domain.models.Schedule
import com.markettwits.club.info.domain.models.Statistic
import com.markettwits.club.info.domain.models.Trainer
import com.markettwits.club.info.domain.models.Training
import com.markettwits.club.registration.domain.WorkoutPrice
import com.markettwits.club.registration.domain.WorkoutPriceForm
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.core_ui.items.extensions.fetchFifth
import com.markettwits.core_ui.items.extensions.flatMap
import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.profile.api.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClubRepositoryBase(
    private val clubservice: SportSauceClubsApi,
    private val subscriptionMapper: SubscriptionMapper,
    private val clubInfoMapper: ClubInfoMapper,
    private val authDataSource: AuthDataSource
) : ClubRepository {

    override suspend fun subscriptions(): Flow<List<SubscriptionItems>> = flow {
        val subscriptions = subscriptionMapper.map(clubservice.subscription())
        emit(subscriptions)
    }

    override suspend fun workoutRegistrationUserData(): WorkoutRegistrationForm =
        subscriptionMapper.map(authDataSource.sharedUser())

    override suspend fun workoutRegistration(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit> =
        runCatching {
            clubservice.workoutRequest(subscriptionMapper.map(workoutRegistrationForm))
        }

    override suspend fun workoutRegistrationPrice(
        workoutPriceForm: WorkoutPriceForm
    ): Result<WorkoutPrice> = runCatching {
        val result = clubservice.workoutRequestPrice(subscriptionMapper.map(workoutPriceForm))
        subscriptionMapper.map(result)
    }

    override suspend fun clubInfo(): Result<List<ClubInfo>> = runCatching {
        val cloud =
            fetchFifth<List<Trainer>, List<Question>, List<Statistic>, List<Training>, List<Schedule>>(
                { clubInfoMapper.mapTrainers(clubservice.trainers()) },
                { clubInfoMapper.mapQuestions(clubservice.questions()) },
                { clubInfoMapper.mapStatistics(clubservice.clubSettings()) },
                { clubInfoMapper.mapTraining(clubservice.workout()) },
                { clubInfoMapper.mapSchedule(clubservice.schedule()) }
            )
        listOf(
            ClubInfo.Commands(cloud.first),
            ClubInfo.Questions(cloud.second),
            ClubInfo.Statistics(cloud.third),
            ClubInfo.Trainings(cloud.fourth),
            ClubInfo.Schedules(cloud.fifth)
        )
    }


}