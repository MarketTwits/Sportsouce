package com.markettwits.sportsouce.start.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApiBase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class DonationCheckApiTest {
    @Test fun checkUsesConfiguredHostStartIdAndCurrentUserAuthorization() = runTest {
        var requests = 0
        val client = HttpClient(MockEngine { request ->
            requests++
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("api.example.test", request.url.host)
            assertEquals("/donations/check", request.url.encodedPath)
            assertEquals(setOf("startId"), request.url.parameters.names())
            val unpaid = request.url.parameters["startId"] == "326"
            assertEquals(if (unpaid) "Bearer current-user" else null, request.headers[HttpHeaders.Authorization])
            respond(if (unpaid) """{"risk":"registered_unpaid","registration":{"groupId":52803,"paymentStatus":0,"amountKopecks":50000,"canPay":true},"matchedAmountKopecks":null}"""
                else """{"risk":"none","registration":null,"matchedAmountKopecks":null}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { defaultRequest { url("https://api.example.test/") } }
        try {
            val api = SportSauceStartApiBase(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = JsonProviderBase().provide()
            })
            val unpaid = api.checkDonation(326, "current-user")
            assertEquals("registered_unpaid", unpaid.risk)
            assertEquals(52803, unpaid.registration?.groupId)
            assertEquals(50000L, unpaid.registration?.amountKopecks)
            assertTrue(unpaid.registration?.canPay == true)
            val none = api.checkDonation(315, null)
            assertEquals("none", none.risk)
            assertNull(none.registration)
            assertEquals(2, requests)
        } finally { client.close() }
    }
}
