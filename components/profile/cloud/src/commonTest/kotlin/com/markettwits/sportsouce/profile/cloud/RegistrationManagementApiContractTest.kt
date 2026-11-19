package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.core_cloud.provider.ServiceResponseException
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationCorrection
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationTransfer
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.*
import io.ktor.http.content.TextContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.*
import kotlin.test.*

class RegistrationManagementApiContractTest {
    private val json = JsonProviderBase().provide()

    @Test
    fun readsAllRegistrationsWhenTheServerCapsPageSize() = runTest {
        val seenOffsets = mutableListOf<Int>()
        val provider = provider { request ->
            assertEquals("/user/startsByUserId/42", request.url.encodedPath)
            val offset = request.url.parameters["skipCount"]?.toInt() ?: 0
            seenOffsets += offset
            respond("""{"count":3,"rows":[{"id":${100 + offset},"combo_id":null}]}""")
        }
        val orders = SportSauceNetworkProfileApi(provider).userRegistries(42, "token")
        assertEquals(listOf(0, 1, 2), seenOffsets)
        assertEquals(listOf(100, 101, 102), orders.map { it.id })
    }

    @Test
    fun separatesMemberIdsOrderIdsPaymentGroupIdsAndInvitationIds() = runTest {
        val provider = provider { request ->
            val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            when (request.url.encodedPath) {
                "/identity/registrations/71/corrections" -> {
                    assertEquals(HttpMethod.Post, request.method)
                    assertEquals(71, body.getValue("memberStartId").jsonPrimitive.int)
                    assertEquals("birth_year", body.getValue("type").jsonPrimitive.content)
                    assertEquals(1998, body.getValue("birthYear").jsonPrimitive.int)
                    respond("""{"id":9,"status":"pending"}""")
                }
                "/identity/registrations/71/transfer" -> {
                    assertEquals(85, body.getValue("targetUserId").jsonPrimitive.int)
                    assertFalse("inviteEmail" in body)
                    assertEquals("keep", body.getValue("options").jsonArray.single().jsonObject.getValue("action").jsonPrimitive.content)
                    respond("""{"expiresAt":"2026-12-01T10:00:00Z"}""")
                }
                "/member-start/payment/82/yokassa-refund" -> {
                    assertEquals(70, body.getValue("refund_percent").jsonPrimitive.int)
                    respond("", HttpStatusCode.NoContent)
                }
                "/member-start/80/repayment" -> {
                    assertEquals(HttpMethod.Put, request.method)
                    assertEquals("start2026", body.getValue("promocode").jsonPrimitive.content)
                    respond("""{"success":true,"payment":{"formUrl":"https://example.org/pay"}}""")
                }
                "/identity/invitations/91/pay-fee" -> {
                    assertEquals("card", body.getValue("method").jsonPrimitive.content)
                    respond("""{"formUrl":"https://example.org/fee"}""")
                }
                else -> error("Unexpected registration route: ${request.url.encodedPath}")
            }
        }
        val api = SportSauceRegistrationManagementApi(provider)
        api.correct(RegistrationCorrection(71, "birth_year", "Иван", "Тестов", "", 1998), "token")
        api.transfer(71, RegistrationTransfer("gift", listOf(RegistrationTransfer.Option(20, "keep")), targetUserId = 85), "token")
        api.refund(82, 70, "token")
        assertEquals("https://example.org/pay", api.repay(80, "start2026", "token").paymentUrl)
        assertEquals("https://example.org/fee", api.payTransfer(91, "card", "token"))
    }

    @Test
    fun externalRegistrationUsesPagedAccountListingAndKeepsCreateAndEditPayloads() = runTest {
        val changes = mutableListOf<HttpMethod>()
        val api = SportSauceRegistrationManagementApi(provider { request ->
            if (request.method == HttpMethod.Get) {
                assertEquals("/external-registrations", request.url.encodedPath)
                assertEquals("42", request.url.parameters["userId"])
                assertEquals("10", request.url.parameters["skipCount"])
                assertEquals("10", request.url.parameters["maxResultCount"])
                respond("""{"count":12,"rows":[{"id":52,"startName":"Trail","distanceName":"10 km","startDate":"2024-03-02","url":"https://organizer.example","poster":{"id":7,"path":"https://cdn.example/posters","name":"trail.jpg"}}]}""")
            } else {
                changes.add(request.method)
                val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
                assertEquals(if (request.method == HttpMethod.Post) "/external-registrations" else "/external-registrations/52",
                    request.url.encodedPath)
                assertEquals("2024-03-02", body.getValue("startDate").jsonPrimitive.content)
                assertEquals(JsonNull, body["posterId"])
                respond("", HttpStatusCode.NoContent)
            }
        })
        val page = api.external(42, 10, "token")
        assertEquals(12, page.count)
        assertEquals("https://cdn.example/posters/trail.jpg", page.rows.single().poster?.url)
        val payload = buildJsonObject {
            put("startDate", "2024-03-02")
            put("posterId", JsonNull)
        }
        api.saveExternal(null, payload, "token")
        api.saveExternal(52, payload, "token")
        assertEquals(listOf(HttpMethod.Post, HttpMethod.Put), changes)
    }

    @Test
    fun acceptsEmptyExternalDeleteAndReportsRejectedOptionPayment() = runTest {
        val provider = provider { request ->
            if (request.method == HttpMethod.Delete) {
                assertEquals("/external-registrations/7", request.url.encodedPath)
                respond("", HttpStatusCode.NoContent)
            } else {
                assertEquals("/member-start/80/unpaid-additional-fields/payment", request.url.encodedPath)
                respond("""{"success":false,"message":["Опция уже оплачена"]}""")
            }
        }
        val api = SportSauceRegistrationManagementApi(provider)
        api.deleteExternal(7, "token")
        val failure = assertFailsWith<IllegalStateException> { api.payOptions(80, buildJsonObject {}, "token") }
        assertEquals("Опция уже оплачена", failure.message)
    }

    @Test
    fun preservesErrorEnvelopesBeforeDecodingPricesOptionsAndRefunds() = runTest {
        val api = SportSauceRegistrationManagementApi(provider { request ->
            when (request.url.encodedPath) {
                "/member-start/80/price" ->
                    respond("""{"ok":false,"detail":"Промокод больше не действует"}""")
                "/member-start/80/unpaid-additional-fields" ->
                    respond("""{"errors":{"registration":["Нет доступа к опциям","Регистрация отменена"]}}""")
                "/member-start/payment/82/yokassa-refund" ->
                    respond("""{"success":false,"error":{"message":"Возврат отклонён"}}""")
                else -> error("Unexpected registration route: ${request.url.encodedPath}")
            }
        })
        val quote = assertFailsWith<ServiceResponseException> { api.price(80, "", "token") }
        assertEquals("Промокод больше не действует", quote.userMessage)
        val options = assertFailsWith<ServiceResponseException> { api.options(80, "token") }
        assertEquals("Нет доступа к опциям\nРегистрация отменена", options.userMessage)
        val refund = assertFailsWith<ServiceResponseException> { api.refund(82, 70, "token") }
        assertEquals("Возврат отклонён", refund.userMessage)
    }

    private fun provider(handler: MockRequestHandler) = object : HttpClientProvider {
        private val client = HttpClient(MockEngine) {
            expectSuccess = true
            engine {
                addHandler { request ->
                    assertEquals("Bearer token", request.headers[HttpHeaders.Authorization])
                    handler(this, request)
                }
            }
            install(ContentNegotiation) { json(this@RegistrationManagementApiContractTest.json) }
        }
        override fun provide(loggerEnabled: Boolean) = client
        override fun json(): Json = this@RegistrationManagementApiContractTest.json
    }
}
