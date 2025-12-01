package com.markettwits.core_cloud.provider

import com.markettwits.buildkonfig.BuildKonfig
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
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
                logger = Logger.SIMPLE
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
