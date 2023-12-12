package ru.alexpanov.core_network.api

import ru.alexpanov.core_network.model.all.StartsCloud
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.path
import kotlinx.serialization.json.Json
import ru.alexpanov.core_network.provider.HttpClientProvider2

class SportSouceReposiotryImpl(
    private val httpClient: HttpClientProvider2
) : SportsouceApi {
    override suspend fun fetchStarts(): StartsCloud {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
            ignoreUnknownKeys = true
            useAlternativeNames = false
        }
        val serializer = StartsCloud.serializer()
        val response = httpClient.get().get("start")
        return json.decodeFromString(serializer, response.body<String>())
    }
}