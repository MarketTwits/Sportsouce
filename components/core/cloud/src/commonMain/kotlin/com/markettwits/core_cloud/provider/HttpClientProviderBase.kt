package com.markettwits.core_cloud.provider

import com.markettwits.buildkonfig.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientProviderBase(
    private val json: Json,
    private val baseUrl: String,
) : HttpClientProvider {

    override fun provide(loggerEnabled: Boolean) = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        if (loggerEnabled) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
            socketTimeoutMillis = 30000
        }
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url.takeFrom(
                URLBuilder()
                    .takeFrom(baseUrl)
                    .apply { encodedPath += url.encodedPath }
            )
        }
    }

    override fun json(): Json = json
}



fun SportSauceHttpClientProvider(
    baseUrl: String = SportSauceBaseUrl,
    json: Json = JsonProviderBase().provide(),
) : HttpClientProvider {
    return HttpClientProviderBase(
        json = json,
        baseUrl = baseUrl,
    )
}

val SportSauceBaseUrl = BuildKonfig.SPORTSAUCE_API_PATH
