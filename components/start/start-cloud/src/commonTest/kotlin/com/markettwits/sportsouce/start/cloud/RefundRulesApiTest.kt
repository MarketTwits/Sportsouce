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

class RefundRulesApiTest {
    @Test fun effectiveRulesDecodeGlobalAndStartContractsWithoutAuthenticationOrExtraRequests() = runTest {
        val json = JsonProviderBase().provide()
        for (source in listOf("global", "start")) {
            var requests = 0
            val client = HttpClient(MockEngine { request ->
                requests++
                assertEquals(HttpMethod.Get, request.method)
                assertEquals("/start/318/refund-rules/effective", request.url.encodedPath)
                assertTrue(request.url.parameters.isEmpty())
                respond("""{"source":"$source","start_id":318,"start_date":"2026-10-25T10:00:00.000Z","rules":[{"id":4,"start_id":318,"refund_percent":50,"days_before_start":50,"description":"Индивидуальные условия","createdAt":"2026-10-25T06:11:22.000Z"},{"id":3,"refund_percent":0,"days_before_start":0,"description":null}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"))
            }) { defaultRequest { url("https://api.example.test/") } }
            try {
                val api = SportSauceStartApiBase(object : HttpClientProvider {
                    override fun provide(loggerEnabled: Boolean) = client
                    override fun json() = json
                })
                val result = api.refundRules(318)
                assertEquals(source, result.source)
                assertEquals(318, result.startId)
                assertEquals("2026-10-25T10:00:00.000Z", result.startDate)
                assertEquals(50, result.rules.first().refundPercent)
                assertEquals(50, result.rules.first().daysBeforeStart)
                assertEquals("Индивидуальные условия", result.rules.first().description)
                assertNull(result.rules.last().description)
                assertEquals(1, requests)
            } finally { client.close() }
        }
    }
}
