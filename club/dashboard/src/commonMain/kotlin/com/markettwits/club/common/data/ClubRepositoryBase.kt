package com.markettwits.club.common.data

import com.markettwits.club.cloud.api.SportSauceClubsApi
import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.common.domain.mapper.ClubInfoMapper
import com.markettwits.club.common.domain.mapper.SubscriptionMapper
import com.markettwits.club.dashboard.domain.SubscriptionItems
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClubRepositoryBase(
    private val service: SportSauceClubsApi,
    private val subscriptionMapper: SubscriptionMapper,
    private val clubInfoMapper: ClubInfoMapper
) : ClubRepository {
    override suspend fun subscriptions(): Flow<List<SubscriptionItems>> = flow {
        val subscriptions = subscriptionMapper.map(service.subscription())
        emit(subscriptions)
    }

    override suspend fun workoutRegistration(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit> =
        runCatching {
            service.workoutRequest(subscriptionMapper.map(workoutRegistrationForm))
        }

    override suspend fun clubInfo(): Result<List<ClubInfo>> = runCatching {
        listOf(
            ClubInfo.Commands(clubInfoMapper.mapTrainers(service.trainers())),
            ClubInfo.Questions(clubInfoMapper.mapQuestions(service.questions())),
            ClubInfo.Statistics(clubInfoMapper.mapStatistics(service.clubSettings())),
            ClubInfo.Trainings(clubInfoMapper.mapTraining(service.workout())),
        )
    }


}