package com.markettwits.sportsouce.notification.impl.push

import com.markettwits.sportsouce.auth.service.api.AuthSession
import com.markettwits.sportsouce.auth.service.api.CachedAuthSession
import com.markettwits.sportsouce.notification.impl.data.PushDeviceApi
import com.markettwits.sportsouce.notification.impl.domain.PushDeviceRepository
import com.markettwits.sportsouce.notification.impl.domain.PushMessage
import com.markettwits.sportsouce.notification.impl.domain.PushRegistration
import com.markettwits.sportsouce.notification.impl.domain.PushRegistrationPlatform

import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class PushContractTest {
    @Test
    fun dataMessageRequiresAnExplicitRecipientAndDeliveryId() {
        val data = mapOf(
            "kind" to "sportsauce_notification",
            "deliveryId" to "8451",
            "recipientUserId" to "42",
            "title" to "Изменилось расписание старта",
            "body" to "Проверьте время старта.",
        )
        val message = assertNotNull(PushMessage.parse(data))
        assertEquals(8451, message.deliveryId)
        assertEquals(42, message.recipientUserId)
        assertNull(PushMessage.parse(data - "recipientUserId"))
        assertNull(PushMessage.parse(data - "title"))
        assertNull(PushMessage.parse(data + ("deliveryId" to "0")))
        assertNull(PushMessage.parse(data + ("kind" to "unknown")))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun pushBindingTracksPermissionTokenRotationAndSignOut() = runTest {
        val session = AuthSession(42, "sportsauce-access-token")
        val sessions = object : CachedAuthSession {
            override val userIds = MutableStateFlow<Int?>(null)
            override suspend fun current() = session.takeIf { userIds.value == it.userId }
        }
        val platform = object : PushRegistrationPlatform {
            var allowed = false
            var fcmToken = "initial"
            var installation: String? = null
            var registration: Pair<Triple<String, Int, String>, Instant>? = null
            override fun isSupported() = true
            override fun canShowNotifications() = allowed
            override fun setAutoInit(enabled: Boolean) = Unit
            override suspend fun token() = fcmToken
            override suspend fun installationId(create: Boolean): String? {
                if (create && installation == null) installation = "installation"
                return installation
            }
            override fun dismissAccountNotifications() = Unit
            override fun registeredAt(installationId: String, userId: Int, token: String): Instant? =
                registration?.takeIf { it.first == Triple(installationId, userId, token) }?.second

            override fun saveRegistration(installationId: String, userId: Int, token: String, registeredAt: Instant) {
                registration = Triple(installationId, userId, token) to registeredAt
            }
            override fun clearRegistration() { registration = null }
        }
        val operations = mutableListOf<String>()
        var stalledToken: String? = null
        val devices = object : PushDeviceRepository {
            override suspend fun register(installationId: String, accessToken: String, fcmToken: String) {
                assertEquals("installation", installationId)
                assertEquals(session.accessToken, accessToken)
                operations += fcmToken
                if (fcmToken == stalledToken) awaitCancellation()
            }
            override suspend fun unregister(installationId: String, accessToken: String) {
                assertEquals("installation", installationId)
                assertEquals(session.accessToken, accessToken)
                operations += "unregister"
            }
        }
        var now = Instant.parse("2026-11-25T00:00:00Z")
        val clock = object : Clock { override fun now() = now }
        fun startRegistration() = PushRegistration(backgroundScope, sessions, { error("Unexpected refresh") }, devices, platform, clock)
        val registration = startRegistration()

        sessions.userIds.value = session.userId
        runCurrent()
        assertEquals(emptyList(), operations)

        platform.allowed = true
        registration.refresh()
        runCurrent()
        val restartedRegistration = startRegistration()
        runCurrent()
        assertEquals(listOf("initial"), operations)

        now += 7.days
        restartedRegistration.refresh()
        runCurrent()
        assertEquals(listOf("initial", "initial"), operations)

        platform.fcmToken = "rotated"
        restartedRegistration.refresh()
        runCurrent()
        stalledToken = "in-flight"
        platform.fcmToken = stalledToken
        restartedRegistration.refresh()
        runCurrent()
        platform.allowed = false
        restartedRegistration.deliverySettingsChanged()
        runCurrent()
        assertEquals(listOf("initial", "initial", "rotated", "in-flight", "unregister"), operations)

        platform.allowed = true
        platform.fcmToken = "rotated"
        restartedRegistration.deliverySettingsChanged()
        runCurrent()
        restartedRegistration.beforeSignOut(session)
        assertFalse(restartedRegistration.isCurrentAccount(session.userId))
        restartedRegistration.refresh()
        runCurrent()
        assertEquals(listOf("initial", "initial", "rotated", "in-flight", "unregister", "rotated", "unregister"), operations)
    }

    @Test
    fun pushDeviceRequestsUseTheSameInstallationAndSportsauceCredentials() = runTest {
        val requests = mutableListOf<HttpRequestData>()
        val engine = MockEngine {
            requests += it
            respond("", HttpStatusCode.NoContent)
        }
        val client = HttpClient(engine) {
            expectSuccess = true
            defaultRequest {
                url("https://sportsauce.example/")
            }
            install(ContentNegotiation) { json() }
        }
        val api = PushDeviceApi(object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean): HttpClient {
                assertFalse(loggerEnabled)
                return client
            }
            override fun json(): Json = Json
        })
        try {
            val id = "5765444e-af5a-4380-a40b-7d1d6be38685"
            api.register(id, "sportsauce-access-token", "first-fcm-token")
            api.register(id, "sportsauce-access-token", "rotated-fcm-token")
            api.unregister(id, "sportsauce-access-token")

            assertEquals(listOf(HttpMethod.Put, HttpMethod.Put, HttpMethod.Delete), requests.map { it.method })
            requests.forEach {
                assertEquals("/notifications/push-devices/$id", it.url.encodedPath)
                assertEquals("Bearer sportsauce-access-token", it.headers[HttpHeaders.Authorization])
            }
            listOf("first-fcm-token", "rotated-fcm-token").forEachIndexed { index, token ->
                val body = (requests[index].body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
                assertEquals(buildJsonObject { put("fcmToken", token) }, Json.parseToJsonElement(body))
            }
        } finally {
            client.close()
        }
    }
}
