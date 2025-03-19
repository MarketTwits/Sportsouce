package com.markettwits.news.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.news.cloud.model.NetworkNews
import io.ktor.client.call.body
import io.ktor.client.request.get

class SportSauceNewsNetworkApi(
    httpClient: HttpClientProvider,
    isLoggerEnabled: Boolean = true,
) {
    private val json = httpClient.json()

    private val client = httpClient.provide(isLoggerEnabled)

    suspend fun news(): NetworkNews {
        val response = client.get("news")
        return json.decodeFromString(response.body())
    }

}