package com.markettwits.cloud.api

import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class TimeApiImpl(
    private val httpClient: HttpClientProvider
) : TimeApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    override suspend fun currentTime(): TimeRemote {
        val response =
            client.get("api/Time/current/zone?timeZone=$baseTimeZone")
        return json.decodeFromString(response.body())
    }
    private companion object{
        private const val baseTimeZone = "Asia/Novosibirsk"
    }
}
