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

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        ignoreUnknownKeys = true
        useAlternativeNames = false
    }
    override suspend fun fetchStarts(): StartsCloud {
        val serializer = StartsCloud.serializer()
        val response = httpClient.get().get("start")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStartsWithFilter(): StartsCloud {
        val serializer = StartsCloud.serializer()
        val response = httpClient.get().get("start?skipCount=6&maxResultCount=6&group=true&name=%D0%9B%D0%B5%D1%82%D0%BE+2024,%D0%9B%D0%B5%D1%82%D0%BE+2023&year=&kindOfSports=&city=&fromDate=&toDate=&status=0,6")
        return json.decodeFromString(serializer, response.body<String>())
    }
}