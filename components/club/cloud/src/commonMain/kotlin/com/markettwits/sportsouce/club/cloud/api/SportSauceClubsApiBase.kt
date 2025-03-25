package com.markettwits.sportsouce.club.cloud.api

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.club.cloud.models.club_settings.ClubSettingsRemote
import com.markettwits.sportsouce.club.cloud.models.club_settings.ClubSettingsRemoteRow
import com.markettwits.sportsouce.club.cloud.models.questions.QuestionRemote
import com.markettwits.sportsouce.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.sportsouce.club.cloud.models.schedule.ScheduleRemote
import com.markettwits.sportsouce.club.cloud.models.schedule.ScheduleRemoteRow
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionItemsRemote
import com.markettwits.sportsouce.club.cloud.models.trainer.TrainersRemote
import com.markettwits.sportsouce.club.cloud.models.trainer.TrainersRemoteRow
import com.markettwits.sportsouce.club.cloud.models.workout.WorkoutRemote
import com.markettwits.sportsouce.club.cloud.models.workout.WorkoutRemoteRow
import com.markettwits.sportsouce.club.cloud.models.workout.price.WorkoutPriceRequest
import com.markettwits.sportsouce.club.cloud.models.workout.price.WorkoutPriceResponse
import com.markettwits.sportsouce.club.cloud.models.workout.registration.WorkoutRegistrationRequestRemote
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class SportSauceClubsApiBase(
    httpClient: HttpClientProvider
) : SportSauceClubsApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    override suspend fun trainers(): List<TrainersRemoteRow> {
        val response = client.get("trainer")
        return json.decodeFromString<TrainersRemote>(response.body<String>()).rows
    }

    override suspend fun workout(): List<WorkoutRemoteRow> {
        val response = client.get("workout")
        return json.decodeFromString<WorkoutRemote>(response.body<String>()).rows
    }

    override suspend fun schedule(): List<ScheduleRemoteRow> {
        val response = client.get("schedule")
        return json.decodeFromString<ScheduleRemote>(response.body<String>()).rows
    }

    override suspend fun subscription(): List<SubscriptionItemsRemote> {
        val response = client.get("subscription/grouped")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun clubSettings(): List<ClubSettingsRemoteRow> {
        val response = client.get("club-settings")
        return json.decodeFromString<ClubSettingsRemote>(response.body<String>()).rows
    }

    override suspend fun questions(): List<QuestionRemoteRow> {
        val response = client.get("question")
        return json.decodeFromString<QuestionRemote>(response.body<String>()).rows
    }

    override suspend fun workoutRequest(workout: WorkoutRegistrationRequestRemote) {
        val response = client.post("workout-request") {
            contentType(ContentType.Application.Json)
            setBody(workout)
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun workoutRequestPrice(workoutPriceRequest: WorkoutPriceRequest): WorkoutPriceResponse {
        val response = client.post("workout-request/price"){
            contentType(ContentType.Application.Json)
            setBody(workoutPriceRequest)
        }
        return json.decodeFromString(response.body<String>())
    }
}