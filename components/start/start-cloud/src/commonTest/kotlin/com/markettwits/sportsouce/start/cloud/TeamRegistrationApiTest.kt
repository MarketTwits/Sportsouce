package com.markettwits.sportsouce.start.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.start.cloud.api.register.SportSauceStartRegisterApiBase
import com.markettwits.sportsouce.start.cloud.model.register.on_start.StartRegistrationResponse
import com.markettwits.sportsouce.start.cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterDistance
import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterMember
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import io.ktor.http.content.TextContent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.*
import kotlin.test.*

class TeamRegistrationApiTest {
    @Test fun priceAndFinalRegistrationBothSendTheSelectedAccountId() = runTest {
        val json = Json { ignoreUnknownKeys = true }
        val paths = mutableListOf<String>()
        val client = HttpClient(MockEngine { request ->
            assertEquals(HttpMethod.Post, request.method)
            assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
            val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            val member = body["distances"]!!.jsonArray.single().jsonObject["member"]!!.jsonArray.single().jsonObject
            assertEquals(JsonPrimitive(1775), member["participant_user_id"])
            assertEquals("1972-01-17", member["birthday"]!!.jsonPrimitive.content)
            assertEquals(326, body["start_id"]!!.jsonPrimitive.int)
            assertEquals(468, body["distances"]!!.jsonArray.single().jsonObject["distance_id"]!!.jsonPrimitive.int)
            assertEquals(491, member["stage_id"]!!.jsonPrimitive.int)
            assertEquals("+7 (900) 000-00-01", member["phone"]!!.jsonPrimitive.content)
            assertEquals("runner@example.test", member["email"]!!.jsonPrimitive.content)
            assertFalse(member.containsKey("user_id"))
            paths += request.url.encodedPath
            val result = if (request.url.encodedPath.endsWith("/price")) {
                assertFalse(body.containsKey("registration_without_payment"))
                """{"priceWithoutDiscount":50000,"additionalFieldsPrice":0,"totalPrice":50000,"isPaymentRequired":true,"isPersonalDiscountApplied":false}"""
            } else {
                assertEquals(JsonPrimitive(true), body["registration_without_payment"])
                """{"members":[{"id":55547,"name":"Дмитрий","surname":"Сергеев","patronymic":null,"birthday":"1972-01-17","payment":0,"user_id":2310,"participant_user_id":1775,"registration_source":"third_party","start_id":326,"distance_id":468,"stage_id":491}],"payment":"withoutPayment","success":true}"""
            }
            respond(result, headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
        }) { expectSuccess = true; defaultRequest { url("https://api.sportsauce.ru/") } }
        val api = SportSauceStartRegisterApiBase(object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean) = client
            override fun json() = json
        })
        val request = StartRegisterPriceRequest(null, listOf(StartRegisterDistance(468, listOf(StartRegisterMember(
            answers = emptyList(), birthday = "1972-01-17", city = "Новосибирск", gender = "Мужской", name = "Дмитрий",
            stageId = 491, surname = "Сергеев", team = "СпортСоюз", participantUserId = 1775,
            phone = "+7 (900) 000-00-01", email = "runner@example.test")))), promocode = "", startId = 326)
        assertEquals(50000, api.price(request, "test-token").totalPrice)
        val registered = api.register(request.copy(registrationWithoutPayment = true), "test-token")
        assertTrue(registered.success)
        assertEquals(StartRegistrationResponse.Payment.PaymentString("withoutPayment"), registered.payment)
        assertEquals(listOf("/member-start/price", "/member-start"), paths)
        client.close()
    }
}
