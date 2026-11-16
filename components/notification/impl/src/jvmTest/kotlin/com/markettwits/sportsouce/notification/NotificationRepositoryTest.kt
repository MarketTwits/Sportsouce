package com.markettwits.sportsouce.notification

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.auth.cloud.model.common.AuthException
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.notification.impl.data.*
import com.markettwits.sportsouce.notification.impl.domain.*
import com.markettwits.sportsouce.notification.api.model.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import java.lang.reflect.Proxy
import kotlin.test.*

class NotificationRepositoryTest {
    @Test fun pageUsesOffsetAndBearerAndPreservesPlainTextAndUnknownTypes() = runTest {
        fixture { request ->
            assertEquals("Bearer token", request.headers[HttpHeaders.Authorization])
            assertEquals("/notifications", request.url.encodedPath)
            assertEquals("20", request.url.parameters["skipCount"])
            assertEquals("20", request.url.parameters["maxResultCount"])
            assertEquals("createdAt DESC", request.url.parameters["sorting"])
            pageResponse
        }.use { fixture ->
            val page = fixture.repository.page(20, 20)
            assertEquals(45, page.count)
            assertEquals("123 < 456 & 100%\nНовая строка", page.rows.single().notification.text)
            assertEquals(313, page.rows.single().notification.startId)
            assertEquals("Парковая эстафета", page.rows.single().notification.startName)
        }
    }

    @Test fun readingIsIdempotentUpdatesCounterAndAllReadOverlaysCachedPages() = runTest {
        var readCalls = 0
        fixture { request -> when (request.url.encodedPath) {
            "/notifications/unread-count" -> """{"count":20}"""
            "/notifications/20262/read" -> { assertEquals(HttpMethod.Patch, request.method); readCalls++; notificationJson(true) }
            "/notifications/read-all" -> { assertEquals(HttpMethod.Patch, request.method); """{"updated":19}""" }
            else -> pageResponse
        } }.use { fixture ->
            val repo = fixture.repository
            repo.refreshUnreadCount()
            val item = repo.page(0, 20).rows.single()
            repo.read(item)
            repo.read(item)
            assertEquals(1, readCalls)
            assertEquals(19, repo.unreadCount.value)
            assertTrue(repo.readState.value.apply(item).isRead)
            repo.readAll()
            assertEquals(0, repo.unreadCount.value)
            assertTrue(repo.readState.value.apply(item.copy(id = 20000)).isRead)
            assertFalse(repo.readState.value.apply(item.copy(id = 30000, createdAt = "2999-01-01T00:00:00Z")).isRead)
        }
    }

    @Test fun failedWritesDoNotChangeUnreadState() = runTest {
        fixture { request ->
            if (request.method == HttpMethod.Patch) error("connection lost")
            if (request.url.encodedPath.endsWith("unread-count")) """{"count":20}""" else pageResponse
        }.use { fixture ->
            val repo = fixture.repository
            repo.refreshUnreadCount()
            val item = repo.page(0, 20).rows.single()
            assertFails { repo.read(item) }
            assertFails { repo.readAll() }
            assertEquals(20, repo.unreadCount.value)
            assertFalse(repo.readState.value.apply(item).isRead)
        }
    }

    @Test fun guestsSendNoAnonymousRequestsAndConnectionFailuresAreNotGuests() = runTest {
        var calls = 0
        fixture { calls++; pageResponse }.use { fixture ->
            fixture.token = Result.failure(AuthException("No credentials"))
            assertFailsWith<NotificationAuthRequired> { fixture.repository.page(0, 20) }
            assertEquals(0, calls)
            val guestVersion = fixture.repository.readState.value.sessionVersion
            repeat(3) { assertFailsWith<NotificationAuthRequired> { fixture.repository.page(0, 20) } }
            assertEquals(guestVersion, fixture.repository.readState.value.sessionVersion, "An unauthorised page must not restart its pager forever")
            fixture.token = Result.failure(IllegalStateException("network"))
            assertFailsWith<IllegalStateException> { fixture.repository.page(0, 20) }
            fixture.token = Result.failure(CancellationException("left"))
            assertFailsWith<CancellationException> { fixture.repository.page(0, 20) }
            assertEquals(0, calls)
        }
    }

    @Test fun accountChangesClearReadsAndCount() = runTest {
        fixture { request -> when (request.url.encodedPath) {
            "/notifications/unread-count" -> """{"count":20}"""
            "/notifications/20262/read" -> notificationJson(true)
            else -> pageResponse
        } }.use { fixture ->
            val repo = fixture.repository
            repo.refreshUnreadCount()
            val item = repo.page(0, 20).rows.single()
            repo.read(item)
            fixture.account = 900
            repo.page(0, 20)
            assertEquals(0, repo.unreadCount.value)
            assertFalse(repo.readState.value.apply(item).isRead)
        }
    }

    @Test fun lateCountCannotRestorePreviousAccountBadgeAfterClear() = runTest {
        val started = CompletableDeferred<Unit>()
        val response = CompletableDeferred<Unit>()
        fixture { started.complete(Unit); response.await(); """{"count":20}""" }.use { fixture ->
            val refresh = launch { fixture.repository.refreshUnreadCount() }
            started.await()
            fixture.repository.clear()
            response.complete(Unit)
            refresh.join()
            assertEquals(0, fixture.repository.unreadCount.value)
        }
    }

    @Test fun pagingStopsAtReportedCountAndDoesNotRequestAnExtraPage() = runTest {
        var calls = 0
        fixture { calls++; pageResponse.replace("\"count\":45", "\"count\":1") }.use { fixture ->
            val source = NotificationPagingSource(fixture.repository)
            assertEquals(1, source.load(0, 20).size)
            assertTrue(source.load(20, 20).isEmpty())
            assertEquals(1, calls)
        }
    }

    private fun fixture(response: suspend (io.ktor.client.request.HttpRequestData) -> String) = Fixture(response)
    private class Fixture(response: suspend (io.ktor.client.request.HttpRequestData) -> String) : AutoCloseable {
        var token: Result<String> = Result.success("token")
        var account = 2310
        private val client = HttpClient(MockEngine { request -> respond(response(request), headers = headersOf(HttpHeaders.ContentType, "application/json")) }) {
            expectSuccess = true
            defaultRequest { url("https://api.example.test/") }
        }
        private val auth = object : AuthDataSource by unused() {
            override suspend fun updateToken() = token
            override suspend fun userID() = Result.success(account)
        }
        val repository = NotificationRepositoryBase(NotificationRemoteApi(object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean) = client
            override fun json() = JsonProviderBase().provide()
        }), auth)
        override fun close() { client.close() }
    }

    private companion object {
        fun notificationJson(read: Boolean = false) = """{"id":20262,"isRead":$read,"createdAt":"2026-10-11T09:00:07.000Z","notification":{"title":"Поделитесь впечатлениями о старте","text":"123 < 456 & 100%\nНовая строка","type":"auto_start_review","templateKind":"start_completed","relatedStartId":313,"relatedStartName":"Парковая эстафета","unknownField":"ignored"}}"""
        val pageResponse = """{"count":45,"rows":[${notificationJson()}]}"""
        inline fun <reified T> unused(): T = Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
            error("Unexpected call: ${method.name}")
        } as T
    }
}
