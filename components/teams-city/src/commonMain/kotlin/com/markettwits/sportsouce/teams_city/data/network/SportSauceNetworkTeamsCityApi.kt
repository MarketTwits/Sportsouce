package com.markettwits.sportsouce.teams_city.data.network;

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.teams_city.data.network.model.NetworkCities
import com.markettwits.sportsouce.teams_city.data.network.model.NetworkTeams
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class SportSauceNetworkTeamsCityApi(
    httpClient: HttpClientProvider
) {
    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    suspend fun teams(): NetworkTeams {
        val response = client.get("team")
        return json.decodeFromString(response.body<String>())
    }

    suspend fun cities(withStarts: Boolean): NetworkCities {
        val response = if (withStarts) {
            client.get("city?withStarts=true")
        } else {
            client.get("city")
        }
        return json.decodeFromString(response.body<String>())
    }
}