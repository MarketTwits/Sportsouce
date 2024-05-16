package com.markettwits.club.info.data

import com.markettwits.club.cloud.api.SportSauceClubsApi
import com.markettwits.club.dashboard.domain.model.SubscriptionItems
import com.markettwits.club.info.domain.ClubInfoMapper
import com.markettwits.club.info.domain.ClubInfoRepository
import com.markettwits.club.info.domain.models.ClubInfo
import com.markettwits.club.info.domain.models.Trainer
import kotlinx.coroutines.flow.Flow

class ClubInfoRepositoryBase(
    private val service: SportSauceClubsApi,
    private val mapper: ClubInfoMapper
) : ClubInfoRepository {
    override suspend fun trainers(): Result<List<Trainer>> = runCatching {
        mapper.mapTrainers(service.trainers())
    }

    override suspend fun clubInfo(): Result<List<ClubInfo>> = runCatching {
        listOf(
            ClubInfo.Commands(mapper.mapTrainers(service.trainers())),
            ClubInfo.Questions(mapper.mapQuestions(service.questions())),
            ClubInfo.Statistics(mapper.mapStatistics(service.clubSettings())),
            ClubInfo.Trainings(mapper.mapTraining(service.workout())),
        )
    }
}