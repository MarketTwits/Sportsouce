package com.markettwits.club.cloud.api

import com.markettwits.club.cloud.models.club_settings.ClubSettingsRemoteRow
import com.markettwits.club.cloud.models.questions.QuestionRemote
import com.markettwits.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.club.cloud.models.schedule.ScheduleRemoteRow
import com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.club.cloud.models.trainer.TrainersRemoteRow
import com.markettwits.club.cloud.models.workout.WorkoutRemoteRow
import com.markettwits.club.cloud.models.workout.price.WorkoutPriceRequest
import com.markettwits.club.cloud.models.workout.price.WorkoutPriceResponse
import com.markettwits.club.cloud.models.workout.registration.WorkoutRegistrationRequestRemote


interface SportSauceClubsApi {

    suspend fun trainers(): List<TrainersRemoteRow>

    suspend fun workout(): List<WorkoutRemoteRow>

    suspend fun schedule(): List<ScheduleRemoteRow>

    suspend fun subscription(): List<SubscriptionItemsRemote>

    suspend fun clubSettings(): List<ClubSettingsRemoteRow>

    suspend fun questions(): List<QuestionRemoteRow>

    suspend fun workoutRequest(workout: WorkoutRegistrationRequestRemote)

    suspend fun workoutRequestPrice(workoutPriceRequest: WorkoutPriceRequest) : WorkoutPriceResponse
}