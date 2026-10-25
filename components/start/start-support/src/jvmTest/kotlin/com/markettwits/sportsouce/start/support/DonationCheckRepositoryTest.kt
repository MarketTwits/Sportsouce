package com.markettwits.sportsouce.start.support

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationCheckRemote
import com.markettwits.sportsouce.start.support.data.StartSupportRepositoryBase
import com.markettwits.sportsouce.start.support.data.mapper.StartSupportMapperBase
import com.markettwits.sportsouce.start.support.domain.StartSupportUseCaseBase
import com.markettwits.sportsouce.start.support.domain.SupportAuthorizationRequired
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import java.lang.reflect.Proxy
import kotlin.test.*

class DonationCheckRepositoryTest {
    @Test fun checkUsesTokenDoesNotTreatAuthNetworkFailureAsGuestAndPaymentUsesGroupId() = runTest {
        val tokens = mutableListOf<String?>()
        var token: Result<String> = Result.success("current-user")
        val auth = object : AuthDataSource by unused() {
            override suspend fun updateToken() = token
        }
        val api = object : SportSauceStartApi by unused() {
            override suspend fun checkDonation(startId: Int, token: String?): StartDonationCheckRemote {
                assertEquals(326, startId); tokens += token
                return StartDonationCheckRemote("registered_unpaid", StartDonationCheckRemote.Registration(52803, 0, 50000, true))
            }
        }
        var payments = 0
        var paymentUnauthorized = false
        val client = HttpClient(MockEngine { request ->
            payments++
            assertEquals(HttpMethod.Put, request.method)
            assertEquals("/member-start/52803/repayment", request.url.encodedPath)
            assertEquals("Bearer current-user", request.headers[HttpHeaders.Authorization])
            if (paymentUnauthorized) return@MockEngine respond("""{"message":"Unauthorized"}""", HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
            respond("""{"success":true,"payment":{"formUrl":"https://payments.example.test/registration","orderId":"order-52803"}}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { expectSuccess = true; defaultRequest { url("https://api.example.test/") } }
        try {
            val profile = SportSauceNetworkProfileApi(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = JsonProviderBase().provide()
            })
            val repository = StartSupportRepositoryBase(api, StartSupportMapperBase(), auth, profile)
            val check = repository.checkDonation(326).getOrThrow()
            assertEquals(listOf<String?>("current-user"), tokens)
            assertTrue(check.requiresConfirmation)
            assertEquals(50000L, check.registration?.amountKopecks)
            token = Result.failure(IllegalStateException("auth unavailable"))
            assertTrue(repository.checkDonation(326).isFailure)
            assertEquals(1, tokens.size)
            token = Result.failure(AuthException("No credentials"))
            assertIs<SupportAuthorizationRequired>(repository.checkDonation(326).exceptionOrNull())
            assertEquals(1, tokens.size, "Guest must not send an anonymous check")
            token = Result.success("")
            assertIs<SupportAuthorizationRequired>(repository.checkDonation(326).exceptionOrNull())
            token = Result.failure(CancellationException("closed"))
            assertFailsWith<CancellationException> { repository.checkDonation(326) }
            token = Result.success("current-user")
            assertEquals("https://payments.example.test/registration", repository.payRegistration(52803).getOrThrow())
            assertEquals(1, payments)
            paymentUnauthorized = true
            assertIs<SupportAuthorizationRequired>(repository.payRegistration(52803).exceptionOrNull())
            val useCase = StartSupportUseCaseBase(repository)
            listOf("", "0", "-1", "100000", "999999999999999999", "abc").forEach { assertTrue(useCase.validate(it).isFailure, it) }
            assertEquals(500, useCase.validate("500").getOrThrow())
        } finally { client.close() }
    }

    private inline fun <reified T> unused(): T = Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
        error("Unexpected call: ${method.name}")
    } as T
}
