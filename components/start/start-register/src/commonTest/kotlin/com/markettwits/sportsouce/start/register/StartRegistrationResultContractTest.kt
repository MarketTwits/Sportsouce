package com.markettwits.sportsouce.start.register

import com.markettwits.sportsouce.start.cloud.model.register.on_start.StartRegistrationResponse
import com.markettwits.sportsouce.start.register.presentation.registration.common.data.mapper.StartRegisterResultMapper
import kotlinx.serialization.json.Json
import kotlin.test.*

class StartRegistrationResultContractTest {
    private val json = Json { ignoreUnknownKeys = true }
    private val mapper = StartRegisterResultMapper()

    @Test fun teamRegistrationWithoutPaymentCompletesWithoutOpeningAPaymentPage() {
        val response = json.decodeFromString(StartRegistrationResponse.serializer(), """
            {"members":[{"id":55547,"user_id":2310,"participant_user_id":1775,
              "registration_source":"third_party","payment":0,"birthday":"1972-01-17"}],
              "payment":"withoutPayment","success":true}
        """.trimIndent())
        val result = mapper.mapResult(response)
        assertTrue(result.isSuccess)
        assertFalse(result.isError)
        assertEquals("", result.paymentUrl)
    }

    @Test fun anUnconfirmedRegistrationIsNotReportedAsSuccessful() {
        val response = json.decodeFromString(StartRegistrationResponse.serializer(),
            """{"members":[],"payment":"withoutPayment","success":false}""")
        assertFailsWith<IllegalStateException> { mapper.mapResult(response) }
    }

    @Test fun onlinePaymentKeepsItsExistingRedirect() {
        val response = json.decodeFromString(StartRegistrationResponse.serializer(),
            """{"success":true,"payment":{"formUrl":"https://example.test/payment","orderId":"order-123"}}""")
        assertEquals("https://example.test/payment", mapper.mapResult(response).paymentUrl)
        assertFailsWith<IllegalStateException> { mapper.mapResult(response.copy(success = false)) }
    }
}
