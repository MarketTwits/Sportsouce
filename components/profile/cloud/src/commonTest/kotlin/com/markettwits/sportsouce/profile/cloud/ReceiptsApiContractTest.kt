package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ReceiptsApiContractTest {
    @Test fun listUsesAuthenticatedCurrentUserEndpointAndPreservesMajorUnitsAndDuplicateParticipantNames() = runTest {
        val api = SportSauceNetworkReceiptsApi(provider { request ->
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("/member-start/receipts/my", request.url.encodedPath)
            assertTrue(request.url.parameters.isEmpty())
            assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
            respond("""[{"id":42,"orderNumber":"order-42","startName":"Осенний старт","startDate":"25.10.2026","amount":"3901.50","createdAt":"2026-10-20T01:00:00.000Z","participants":["Анна Иванова","Анна Иванова"],"receiptUrl":"/member-start/receipts/42"},{"id":43,"amount":"0.00","participants":[]}]""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        })
        val list = api.receipts("test-token")
        assertEquals(2, list.size)
        assertEquals("3901.50", list.first().amount)
        assertEquals(2, list.first().participants.size)
        assertEquals("0.00", list.last().amount)
    }

    @Test fun originalHtmlIsFetchedWithAuthorizationAndReturnedWithoutRebuildingFinancialDetails() = runTest {
        val html = "<!doctype html><html><head><title>Квитанция</title></head><body><main>ИТОГ: 3901.50</main></body></html>"
        val api = SportSauceNetworkReceiptsApi(provider { request ->
            assertEquals("/member-start/receipts/42", request.url.encodedPath)
            assertEquals("Bearer fresh-token", request.headers[HttpHeaders.Authorization])
            assertEquals("text/html", request.headers[HttpHeaders.Accept])
            respond(html, headers = headersOf(HttpHeaders.ContentType, "text/html; charset=utf-8"))
        })
        assertEquals(html, api.document(42, "fresh-token"))
    }

    @Test fun unauthorizedAndInvalidDocumentsAreFailuresAndEmptyListIsValid() = runTest {
        val api = SportSauceNetworkReceiptsApi(provider { request ->
            when (request.url.encodedPath) {
                "/member-start/receipts/my" -> respond("[]")
                "/member-start/receipts/1" -> respond("Unauthorized", HttpStatusCode.Unauthorized)
                else -> respond("""{"message":"Квитанция недоступна"}""")
            }
        })
        assertTrue(api.receipts("test-token").isEmpty())
        assertFails { api.document(1, "test-token") }
        assertFails { api.document(2, "test-token") }
        assertFails { api.document(0, "test-token") }
    }

    private fun provider(handler: suspend MockRequestHandleScope.(io.ktor.client.request.HttpRequestData) -> io.ktor.client.request.HttpResponseData): HttpClientProvider {
        val client = HttpClient(MockEngine(handler)) { expectSuccess = true; defaultRequest { url("https://api.sportsauce.ru/") } }
        return object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean): HttpClient { assertFalse(loggerEnabled); return client }
            override fun json() = JsonProviderBase().provide()
        }
    }
}
