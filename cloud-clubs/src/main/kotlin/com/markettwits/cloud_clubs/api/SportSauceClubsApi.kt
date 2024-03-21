package com.markettwits.cloud_clubs.api

import com.markettwits.cloud_clubs.models.club_settings.ClubSettingsRemote
import com.markettwits.cloud_clubs.models.schedule.ScheduleRemote
import com.markettwits.cloud_clubs.models.subscription.SubscriptionRemoteItem
import com.markettwits.cloud_clubs.models.trainer.TrainersRemote
import com.markettwits.cloud_clubs.models.workout.WorkoutRemote


interface SportSauceClubsApi {
    suspend fun trainers(): TrainersRemote
    suspend fun workout(): WorkoutRemote
    suspend fun schedule(): ScheduleRemote
    suspend fun subscription(): List<SubscriptionRemoteItem>
    suspend fun clubSettings(): ClubSettingsRemote

}