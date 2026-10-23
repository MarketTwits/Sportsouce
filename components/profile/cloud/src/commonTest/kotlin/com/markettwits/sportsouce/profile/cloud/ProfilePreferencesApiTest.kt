package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.profile.cloud.model.preferences.UpdateProfilePreferencesRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import io.ktor.http.content.TextContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.*
import kotlin.test.*

class ProfilePreferencesApiTest {
    private val json = JsonProviderBase().provide()
    @Test fun getUsesAuthenticatedCompletenessAndReadsOptionalPreferences() = runTest {
        for (preferences in listOf("null", """{"clothingSize":"12","footLengthMm":"23","shoeSizeRu":null,"shoeSizeUs":"9.5","shoeSizeJp":""}""")) {
            val api = SportSauceNetworkPreferencesApi(provider { request ->
                assertEquals(HttpMethod.Get, request.method)
                assertEquals("/identity/me/completeness", request.url.encodedPath)
                assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
                respond("""{"percent":82,"verified":true,"nextSteps":["Размер обуви RU"],"fields":[{"key":"name","label":"Имя","filled":true}],"preferences":$preferences}""", headers = headersOf(HttpHeaders.ContentType, "application/json"))
            })
            val response = api.completeness("test-token")
            assertEquals(82, response.percent)
            if (preferences == "null") assertNull(response.preferences)
            else {
                assertEquals("12", response.preferences?.clothingSize)
                assertEquals("23", response.preferences?.footLengthMm)
                assertEquals("", response.preferences?.shoeSizeRu)
            }
        }
    }

    @Test fun patchSendsExactlyFiveStringsIncludingEmptyValuesAndReadsCanonicalResponse() = runTest {
        val api = SportSauceNetworkPreferencesApi(provider { request ->
            assertEquals(HttpMethod.Patch, request.method)
            assertEquals("/identity/me/preferences", request.url.encodedPath)
            assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
            val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            assertEquals(setOf("clothingSize", "footLengthMm", "shoeSizeUs", "shoeSizeRu", "shoeSizeJp"), body.keys)
            assertEquals(listOf("12", "23", "", "", ""), listOf("clothingSize", "footLengthMm", "shoeSizeUs", "shoeSizeRu", "shoeSizeJp").map { body[it]!!.jsonPrimitive.content })
            assertTrue(body.values.all { it.jsonPrimitive.isString })
            respond("""{"percent":82,"verified":true,"preferences":{"clothingSize":"12","footLengthMm":"23","shoeSizeUs":"","shoeSizeRu":"","shoeSizeJp":""}}""", headers = headersOf(HttpHeaders.ContentType, "application/json"))
        })
        assertEquals("23", api.update(UpdateProfilePreferencesRequest("12", "23", "", "", ""), "test-token").preferences?.footLengthMm)
    }

    @Test fun failedPatchDoesNotDecodeAnErrorBodyAsSuccessfulEmptyPreferences() = runTest {
        val api = SportSauceNetworkPreferencesApi(provider { respond("""{"message":"Ошибка"}""", HttpStatusCode.BadRequest) })
        assertFails { api.update(UpdateProfilePreferencesRequest("", "", "", "", ""), "test-token") }
    }

    private fun provider(handler: MockRequestHandler): HttpClientProvider {
        val client = HttpClient(MockEngine(handler)) {
            expectSuccess = true
            install(ContentNegotiation) { json(json) }
            defaultRequest { url("https://api.sportsauce.ru/") }
        }
        return object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean): HttpClient { assertFalse(loggerEnabled); return client }
            override fun json() = json
        }
    }
}
