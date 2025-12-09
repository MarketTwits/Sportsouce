package com.markettwits.sportsouce.starts.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartFavoritesRequest
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartsRemote
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class SportSauceNetworkStartsApi(
    httpClient: HttpClientProvider
) {

    private val json = httpClient.json()

    private val client = httpClient.provide()

    suspend fun fetchFavorites(userId: String, token: String): NetworkStartsRemote {
        val response = client.get("favorites/$userId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    suspend fun addFavorites(startId: Int, userId: String, token: String) {
        val request = NetworkStartFavoritesRequest(startId = startId, userId = userId)
        client.post("favorites") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
    }

    suspend fun removeFavorites(startId: Int, userId: String, token: String) {
        val request = NetworkStartFavoritesRequest(startId = startId, userId = userId)
        client.delete("favorites") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
    }

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

    suspend fun fetchSeries(seriesId: Int): NetworkStartsRemote {
        val serializer = NetworkStartsRemote.serializer()
        val response = client.get("start/$seriesId/related-start-series")
        return json.decodeFromString(serializer, response.body<String>())
    }

    suspend fun fetchStartMain(): NetworkStartsRemote {
        val response = client.get("start?mainPage=true&openFirst=true")
        return json.decodeFromString(response.body<String>())
    }
}