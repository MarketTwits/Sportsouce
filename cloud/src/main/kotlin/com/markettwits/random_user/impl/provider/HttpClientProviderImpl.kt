package com.markettwits.random_user.impl.provider

import com.markettwits.random_user.api.provider.HttpClientProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
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

class HttpClientProviderImpl(
    private val json: Json,
    private val baseUrl: String
) : HttpClientProvider {
    override fun provideClient() = HttpClient(OkHttp) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HttpClient: $message")
                }
            }
            level = LogLevel.ALL
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
    override fun provideJson(): Json = json
}
