package com.markettwits.sportsouce.start.support

import com.markettwits.cahce.Cache
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.registrations.CachedUserRegistrations
import com.markettwits.sportsouce.profile.cloud.registrations.UserRegistrationsDataSource
import com.markettwits.sportsouce.profile.cloud.registrations.UserStartsRegistrationsCache
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
            val repository = StartSupportRepositoryBase(api, StartSupportMapperBase(), auth, profile, registrations(profile))
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

    @Test fun paidRegistrationFallsBackToProfileWithoutChangingTheDonationRisk() = runTest {
        val auth = object : AuthDataSource by unused() {
            override suspend fun updateToken() = Result.success("current-user")
            override suspend fun cachedUserId() = Result.success(17)
        }
        val api = object : SportSauceStartApi by unused() {
            override suspend fun checkDonation(startId: Int, token: String?) = StartDonationCheckRemote("none")
        }
        var profileRequests = 0
        var profileAvailable = true
        val client = HttpClient(MockEngine { request ->
            profileRequests++
            assertEquals("/user/startsByUserId/17", request.url.encodedPath)
            assertEquals("Bearer current-user", request.headers[HttpHeaders.Authorization])
            if (!profileAvailable) return@MockEngine respondError(HttpStatusCode.ServiceUnavailable)
            respond(
                """{"count":3,"rows":[
                    {"combo_id":null,"start_id":329,"payment":1},
                    {"combo_id":null,"start_id":330,"payment":1,"is_refunded":true},
                    {"combo_id":null,"start_id":331,"payment":3,"price":750000}
                ]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }) { expectSuccess = true; defaultRequest { url("https://api.example.test/") } }
        try {
            val profile = SportSauceNetworkProfileApi(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = JsonProviderBase().provide()
            })
            val repository = StartSupportRepositoryBase(api, StartSupportMapperBase(), auth, profile, registrations(profile))
            val paid = repository.checkDonation(329).getOrThrow()
            assertTrue(paid.hasConfirmedRegistration)
            assertFalse(paid.requiresConfirmation)
            assertFalse(repository.checkDonation(330).getOrThrow().hasRegistration)
            assertTrue(repository.cachedRegistration(329).getOrThrow().check!!.hasConfirmedRegistration)
            assertEquals(750000L, repository.cachedRegistration(331).getOrThrow().check?.registration?.amountKopecks)
            val unpaid = repository.checkDonation(331).getOrThrow()
            assertTrue(unpaid.hasUnpaidRegistration)
            assertFalse(unpaid.registration!!.canPay)
            assertEquals(1, profileRequests)
            assertTrue(repository.checkDonation(329, refreshRegistrations = true).getOrThrow().hasConfirmedRegistration)
            assertEquals(2, profileRequests)
            profileAvailable = false
            assertFalse(repository.checkDonation(329, refreshRegistrations = true).getOrThrow().isRegistrationKnown)
        } finally { client.close() }
    }

    private fun registrations(profile: SportSauceNetworkProfileApi): UserRegistrationsDataSource {
        val cache = object : Cache<CachedUserRegistrations> {
            private var snapshot: CachedUserRegistrations? = null
            override suspend fun get(key: Any) = snapshot
            override suspend fun getList(key: Any) = listOfNotNull(snapshot)
            override suspend fun set(key: Any, value: CachedUserRegistrations) { snapshot = value }
            override suspend fun clear() { snapshot = null }
        }
        return UserRegistrationsDataSource(profile, UserStartsRegistrationsCache(cache) { null })
    }

    private inline fun <reified T> unused(): T = Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
        error("Unexpected call: ${method.name}")
    } as T
}
