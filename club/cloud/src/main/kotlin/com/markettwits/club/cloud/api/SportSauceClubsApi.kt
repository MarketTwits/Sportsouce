package com.markettwits.club.cloud.api

import com.markettwits.club.cloud.models.questions.QuestionRemoteRow


interface SportSauceClubsApi {
    suspend fun trainers(): com.markettwits.club.cloud.models.trainer.TrainersRemote
    suspend fun workout(): com.markettwits.club.cloud.models.workout.WorkoutRemote
    suspend fun schedule(): com.markettwits.club.cloud.models.schedule.ScheduleRemote
    suspend fun subscription(): List<com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote>
    suspend fun clubSettings(): com.markettwits.club.cloud.models.club_settings.ClubSettingsRemote
    suspend fun questions(): QuestionRemoteRow
}