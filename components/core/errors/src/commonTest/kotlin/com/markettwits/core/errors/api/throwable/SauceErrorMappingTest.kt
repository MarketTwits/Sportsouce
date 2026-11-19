package com.markettwits.core.errors.api.throwable

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class SauceErrorMappingTest {
    @Test
    fun usesHttpStatusWhenTheErrorBodyCannotBeParsed() = runTest {
        val client = HttpClient(MockEngine { request ->
            respond("<html>Internal details</html>", HttpStatusCode.fromValue(request.url.encodedPath.substring(1).toInt()))
        }) { expectSuccess = true }
        try {
            suspend fun error(status: Int) =
                assertFailsWith<ResponseException> { client.get("https://api.example.test/$status") }.mapToSauceError()

            assertIs<SauceError.NotFound>(error(404))
            assertIs<SauceError.Connection>(error(408))
            assertContains(error(401).mapToString(), "войдите в аккаунт")
            assertContains(error(403).mapToString(), "нет доступа")
            assertContains(error(429).mapToString(), "Слишком много запросов")
            assertContains(error(503).mapToString(), "Сервис временно недоступен")
        } finally {
            client.close()
        }
    }

    @Test
    fun separatesTimeoutAndDecodingErrorsAndPropagatesCancellation() {
        assertIs<SauceError.Connection>(HttpRequestTimeoutException("https://api.example.test/", 1000).mapToSauceError())
        assertIs<SauceError.JsonConverter>(SerializationException("Invalid JSON").mapToSauceError())
        assertFailsWith<CancellationException> { CancellationException().mapToSauceError() }
    }
}
