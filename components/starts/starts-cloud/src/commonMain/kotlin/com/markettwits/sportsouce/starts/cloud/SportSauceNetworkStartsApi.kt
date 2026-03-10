package com.markettwits.sportsouce.starts.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartFavoritesRequest
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartsRemote
import com.markettwits.sportsouce.starts.cloud.model.StartStatusEntity
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
        return fetchStarts(additionalParameters = request)
    }

    suspend fun fetchActualStarts(): NetworkStartsRemote {
        return fetchStarts(
            limit = 20,
            isGroup = true,
            statuses = listOf(
                StartStatusEntity.REGISTRATION_OPEN,
                StartStatusEntity.ANNOUNCEMENT,
            ),
        )
    }

    suspend fun fetchPasteStarts(): NetworkStartsRemote {
        return fetchStarts(
            limit = 1000,
            isGroup = true,
            statuses = listOf(StartStatusEntity.ENDED),
        )
    }

    suspend fun fetchPreview(): NetworkStartsRemote {
        return fetchStarts(
            limit = 20,
            isGroup = true,
            statuses = listOf(StartStatusEntity.ANNOUNCEMENT),
        )
    }

    suspend fun fetchSeries(seriesId: Int): NetworkStartsRemote {
        val serializer = NetworkStartsRemote.serializer()
        val response = client.get("start/$seriesId/related-start-series")
        return json.decodeFromString(serializer, response.body<String>())
    }

    suspend fun fetchStartMain(): NetworkStartsRemote {
        return fetchStarts(
            isMain = true,
            openFirst = true,
        )
    }

    suspend fun fetchStarts(
        limit: Int? = null,
        offset: Int? = null,
        isMain: Boolean? = null,
        openFirst: Boolean? = null,
        isGroup: Boolean? = null,
        statuses: List<StartStatusEntity> = emptyList(),
        additionalParameters: Map<String, String> = emptyMap(),
    ): NetworkStartsRemote {
        val serializer = NetworkStartsRemote.serializer()
        val response = client.get("start") {
            additionalParameters.forEach { (param, value) ->
                parameter(param, value)
            }
            limit?.let { parameter("maxResultCount", it) }
            offset?.let { parameter("skipCount", it) }
            isMain?.let { parameter("mainPage", it) }
            openFirst?.let { parameter("openFirst", it) }
            isGroup?.let { parameter("group", it) }
            if (statuses.isNotEmpty()) {
                parameter("status", statuses.joinToString(",") { it.id.toString() })
            }
        }
        return json.decodeFromString(serializer, response.body<String>())
    }
}
