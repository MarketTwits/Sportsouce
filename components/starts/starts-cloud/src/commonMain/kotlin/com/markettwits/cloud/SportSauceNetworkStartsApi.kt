package com.markettwits.cloud

import com.markettwits.cloud.model.NetworkStartsRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

class SportSauceNetworkStartsApi(
    private val httpClient: HttpClientProvider
) {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    suspend fun startWithFilter(request: Map<String, String>): NetworkStartsRemote {
        val response = client.get("start") {
            url {
                parameters.apply {
                    request.forEach { (param, value) ->
                        append(param, value)
                    }
                }
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    suspend fun fetchActualStarts(): NetworkStartsRemote {
        val serializer = NetworkStartsRemote.serializer()
        val response = client.get("start?maxResultCount=20&group=true&status=3,2")
        return json.decodeFromString(serializer, response.body<String>())
    }

    suspend fun fetchPasteStarts(): NetworkStartsRemote {
        val serializer = NetworkStartsRemote.serializer()
        val response =
            client.get("start?maxResultCount=1000&group=true&status=6")
        return json.decodeFromString(serializer, response.body<String>())
    }

    suspend fun fetchPreview(): NetworkStartsRemote {
        val serializer = NetworkStartsRemote.serializer()
        val response =
            client.get("start?maxResultCount=20&group=true&status=2")
        return json.decodeFromString(serializer, response.body<String>())
    }

    suspend fun fetchStartMain(): NetworkStartsRemote {
        val response = client.get("start?mainPage=true&openFirst=true")
        return json.decodeFromString(response.body<String>())
    }
}