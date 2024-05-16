package com.markettwits.club.cloud.api

import com.markettwits.club.cloud.models.questions.QuestionRemoteRow
import com.markettwits.club.cloud.models.workout.WorkoutRegistrationRequestRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class SportSauceClubsApiBase(
    private val httpClient: HttpClientProvider
) : SportSauceClubsApi {

    private val json = httpClient.json()
    private val client = httpClient.provide(true)


    override suspend fun trainers(): com.markettwits.club.cloud.models.trainer.TrainersRemote {
        val response = client.get("trainer")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun workout(): com.markettwits.club.cloud.models.workout.WorkoutRemote {
        val response = client.get("workout")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun schedule(): com.markettwits.club.cloud.models.schedule.ScheduleRemote {
        val response = client.get("schedule")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun subscription(): List<com.markettwits.club.cloud.models.subscription.SubscriptionItemsRemote> {
        val response = client.get("subscription/grouped")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun clubSettings(): com.markettwits.club.cloud.models.club_settings.ClubSettingsRemote {
        val response = client.get("club-settings")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun questions(): QuestionRemoteRow {
        val response = client.get("question")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun workoutRequest(workout: WorkoutRegistrationRequestRemote) {
        val response = client.post("workout-request") {
            contentType(ContentType.Application.Json)
            setBody(workout)
        }
        return json.decodeFromString(response.body<String>())
    }
}