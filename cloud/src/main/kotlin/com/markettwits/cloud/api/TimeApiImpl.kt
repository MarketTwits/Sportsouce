package com.markettwits.cloud.api

import com.markettwits.cloud.model.time.TimeRemote
import com.markettwits.cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

class TimeApiImpl(
    private val httpClient: HttpClientProvider
) : TimeApi {
    private val json = httpClient.getJson()
    private val client = httpClient.get()
    override suspend fun currentTime(): TimeRemote {
        val response =
            client.get("api/Time/current/zone?timeZone=$baseTimeZone")
        return json.decodeFromString(response.body())
    }
    private companion object{
        private val baseTimeZone = "Asia/Novosibirsk"
    }
}