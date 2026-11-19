package com.markettwits.core_cloud.provider

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResponseErrorMessageTest {
    @Test
    fun capturesValidationMessagesAndKeepsTheHttpExceptionWhenTheBodyIsInvalid() = runTest {
        val cases = listOf(
            """{"message":"Проверьте номер телефона"}""" to "Проверьте номер телефона",
            """{"message":["Укажите имя","Укажите город"]}""" to "Укажите имя\nУкажите город",
            """{"message":null}""" to null,
            "<html>Bad request</html>" to null,
        )
        for ((body, expected) in cases) {
            val client = HttpClient(MockEngine { respond(body, HttpStatusCode.BadRequest) }) {
                expectSuccess = true
                captureResponseErrorMessages(Json)
            }
            try {
                val error = assertFailsWith<ResponseException> { client.get("https://api.example.test/") }
                assertEquals(HttpStatusCode.BadRequest, error.response.status)
                assertEquals(expected, error.responseErrorMessage)
            } finally {
                client.close()
            }
        }
    }

    @Test
    fun extractsFieldErrorsAndProblemDetailsAcrossHttpStatuses() = runTest {
        val cases = listOf(
            Triple(HttpStatusCode.UnprocessableEntity, """{"errors":{"name":["Укажите имя"],"year":{"message":"Проверьте год"}}}""",
                "Укажите имя\nПроверьте год"),
            Triple(HttpStatusCode.NotFound, """{"detail":"Регистрация не найдена"}""", "Регистрация не найдена"),
            Triple(HttpStatusCode.InternalServerError, """{"error":{"message":"payment record not found"}}""", "payment record not found"),
        )
        for ((status, body, expected) in cases) {
            HttpClient(MockEngine { respond(body, status) }) {
                expectSuccess = true
                captureResponseErrorMessages(Json)
            }.use { client ->
                val error = assertFailsWith<ResponseException> { client.get("https://api.example.test/") }
                assertEquals(status, error.response.status)
                assertEquals(expected, error.responseErrorMessage)
            }
        }
    }
}
