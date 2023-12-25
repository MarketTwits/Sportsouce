package com.markettwits.cloud.api

import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.time.TimeRemote
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import ru.alexpanov.core_network.provider.HttpClientProvider2

class TimeApiImpl(
    private val httpClient: HttpClientProvider2
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