package com.markettwits.cloud_clubs.api

import com.markettwits.cloud_clubs.models.club_settings.ClubSettingsRemote
import com.markettwits.cloud_clubs.models.schedule.ScheduleRemote
import com.markettwits.cloud_clubs.models.subscription.SubscriptionRemoteItem
import com.markettwits.cloud_clubs.models.trainer.TrainersRemote
import com.markettwits.cloud_clubs.models.workout.WorkoutRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class SportSauceClubsApiBase(
    private val httpClient: HttpClientProvider
) : SportSauceClubsApi {

    private val json = httpClient.json()
    private val client = httpClient.provide(true)


    override suspend fun trainers(): TrainersRemote {
        val response = client.get("trainer")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun workout(): WorkoutRemote {
        val response = client.get("workout")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun schedule(): ScheduleRemote {
        val response = client.get("schedule")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun subscription(): List<SubscriptionRemoteItem> {
        val response = client.get("subscription/grouped")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun clubSettings(): ClubSettingsRemote {
        val response = client.get("club-settings")
        return json.decodeFromString(response.body<String>())
    }

}