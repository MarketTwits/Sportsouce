package com.markettwits.sportsouce.profile

import com.markettwits.sportsouce.unauthorized.data.*
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class GuestWelcomeRemoteTest {
    private val url = "https://raw.githubusercontent.com/example/content/main/guest-profile.json"
    private val first = GuestWelcomeVariant("trail", headingLines = listOf("Ваш следующий", "старт — здесь"),
        panelTitle = "Ваш спортивный профиль", description = "Ваши старты и участники", imageUrl = "https://cdn.example.test/trail.webp")
    private val second = first.copy(id = "ski", headingLines = listOf("Зима зовёт", "на лыжню"), imageUrl = "https://cdn.example.test/ski.webp")
    private val config get() = GuestWelcomeConfig(1, variants = listOf(first, second))
    private class Cache(var value: GuestWelcomeCacheEntry? = null) : GuestWelcomeCache {
        override suspend fun read() = value
        override suspend fun write(entry: GuestWelcomeCacheEntry) { value = entry }
    }

    @Test fun publicRawJsonAcceptsTextPlainWithoutAnyApiCredentials() = runTest {
        val client = HttpClient(MockEngine { request ->
            assertEquals(url, request.url.toString())
            assertNull(request.headers[HttpHeaders.Authorization])
            assertNull(request.headers[HttpHeaders.Cookie])
            respond(GuestWelcomeJson.encodeToString(config), headers = headersOf(HttpHeaders.ContentType, "text/plain"))
        })
        try {
            val parsed = GuestWelcomeRemoteSourceBase(client).fetch(url)
            assertEquals(listOf("trail", "ski"), parsed.contents().map { it.id })
        } finally { client.close() }
    }

    @Test fun rejectsInvalidResponsesInsteadOfReplacingTheWorkingConfig() = runTest {
        for ((body, status) in listOf("<html>404</html>" to HttpStatusCode.NotFound,
            "x".repeat(70_000) to HttpStatusCode.OK, "{bad json}" to HttpStatusCode.OK)) {
            val client = HttpClient(MockEngine { respond(body, status) })
            try { assertFails { GuestWelcomeRemoteSourceBase(client).fetch(url) } }
            finally { client.close() }
        }
        assertFails { parseGuestWelcomeConfig(GuestWelcomeJson.encodeToString(config.copy(schemaVersion = 2))) }
        assertFails { config.copy(variants = listOf(first.copy(imageUrl = "http://example.test/a.jpg"))).contents() }
        assertFails { config.copy(variants = listOf(first.copy(headingLines = listOf("")))).contents() }
        assertFails { config.copy(variants = listOf(first.copy(imageAlignmentX = 2f))).contents() }
        assertFails { config.copy(variants = listOf(first, first)).contents() }
        assertEquals(2, parseGuestWelcomeConfig(GuestWelcomeJson.encodeToString(config).dropLast(1) + ",\"futureField\":true}").contents().size)
    }

    @Test fun firstOfflineVisitIsImmediateAndRefreshOnlyChangesTheNextVisit() = runTest {
        val cache = Cache()
        var calls = 0
        var now = 1_000_000L
        val repo = GuestWelcomeRepositoryBase(url, cache, GuestWelcomeRemoteSource { calls++; config }, now = { now }, choose = { 1 })
        val current = repo.cachedContent()
        assertEquals(GuestWelcomeContent.Local, current)
        assertEquals(0, calls, "Reading content must never fetch from the network")
        repo.refresh()
        assertEquals(GuestWelcomeContent.Local, current)
        assertEquals("ski", repo.cachedContent().id)
        repo.refresh()
        assertEquals(1, calls)
        now += 6 * 60 * 60 * 1000L
        repo.refresh()
        assertEquals(2, calls)
    }

    @Test fun networkFailurePreservesStaleContentAndThrottlesRetries() = runTest {
        val cache = Cache(GuestWelcomeCacheEntry(url, 1, config))
        var calls = 0
        var now = 99_000_000L
        val repo = GuestWelcomeRepositoryBase(url, cache, GuestWelcomeRemoteSource { calls++; error("Offline") }, now = { now }, choose = { 0 })
        assertEquals("trail", repo.cachedContent().id)
        repo.refresh(); repo.refresh()
        assertEquals(1, calls)
        assertEquals("trail", repo.cachedContent().id)
        now += 5 * 60 * 1000L
        repo.refresh()
        assertEquals(2, calls)
        assertEquals(config, cache.value?.config)
    }

    @Test fun unchangedJsonRevalidatesWithHeadersAndRetainsContentAcrossRepositoryRecreation() = runTest {
        var time = 1_000_000L
        val lastModified = "Thu, 22 Oct 2026 17:00:00 GMT"
        var calls = 0
        val cache = Cache()
        val client = HttpClient(MockEngine { request ->
            calls++
            if (calls == 1) {
                assertNull(request.headers[HttpHeaders.IfNoneMatch])
                respond(GuestWelcomeJson.encodeToString(config), headers = headersOf(
                    HttpHeaders.ETag to listOf("\"version-1\""), HttpHeaders.LastModified to listOf(lastModified)))
            } else {
                assertEquals("\"version-1\"", request.headers[HttpHeaders.IfNoneMatch])
                assertEquals(lastModified, request.headers[HttpHeaders.IfModifiedSince])
                respond("", status = HttpStatusCode.NotModified)
            }
        }) { expectSuccess = true }
        try {
            val source = GuestWelcomeRemoteSourceBase(client)
            GuestWelcomeRepositoryBase(url, cache, source, now = { time }).refresh()
            assertEquals("\"version-1\"", cache.value?.etag)
            val reopened = GuestWelcomeRepositoryBase(url, cache, source, now = { time }, choose = { 0 })
            assertEquals("trail", reopened.cachedContent().id)
            reopened.refresh()
            assertEquals(1, calls, "A fresh on-disk JSON must not be downloaded again after a restart")
            time += 6 * 60 * 60 * 1000L
            reopened.refresh()
            assertEquals(2, calls)
            assertEquals(config, cache.value?.config)
            assertEquals(time, cache.value?.fetchedAt)
            assertEquals(lastModified, cache.value?.lastModified)
            reopened.refresh()
            assertEquals(2, calls, "304 starts a new freshness interval")
        } finally { client.close() }
    }

    @Test fun aChangedJsonReplacesTheCacheWhileAnInvalidCacheIsRefetchedWithoutConditionalHeaders() = runTest {
        var time = 100_000_000L
        val cache = Cache(GuestWelcomeCacheEntry(url, 1, config, "old"))
        val updated = config.copy(variants = listOf(second))
        var calls = 0
        val client = HttpClient(MockEngine { request ->
            if (calls++ == 0) assertEquals("old", request.headers[HttpHeaders.IfNoneMatch])
            else assertNull(request.headers[HttpHeaders.IfNoneMatch])
            respond(GuestWelcomeJson.encodeToString(updated), headers = headersOf(HttpHeaders.ETag, "new"))
        })
        try {
            val source = GuestWelcomeRemoteSourceBase(client)
            GuestWelcomeRepositoryBase(url, cache, source, now = { time }).refresh()
            assertEquals(updated, cache.value?.config)
            assertEquals("new", cache.value?.etag)
            cache.value = GuestWelcomeCacheEntry(url, time, config.copy(schemaVersion = 7), "corrupt")
            GuestWelcomeRepositoryBase(url, cache, source, now = { time }).refresh()
            assertEquals(2, calls)
            assertEquals(updated, cache.value?.config)
        } finally { client.close() }
    }

    @Test fun emptyEndpointCorruptCacheAndChangedSourceFallBackSafely() = runTest {
        val cache = Cache(GuestWelcomeCacheEntry("https://old.example.test/config.json", 1, config))
        val remote = GuestWelcomeRemoteSource { error("Must not fetch while opening") }
        assertEquals(GuestWelcomeContent.Local, GuestWelcomeRepositoryBase(url, cache, remote).cachedContent())
        val disabled = GuestWelcomeRepositoryBase("", cache, remote)
        assertEquals(GuestWelcomeContent.Local, disabled.cachedContent()); disabled.refresh()
        cache.value = GuestWelcomeCacheEntry(url, 1, config.copy(schemaVersion = 8))
        assertEquals(GuestWelcomeContent.Local, GuestWelcomeRepositoryBase(url, cache, remote).cachedContent())
    }

    @Test fun remoteDisableReturnsToLocalContentAndCancellationIsNotSwallowed() = runTest {
        val cache = Cache()
        val repo = GuestWelcomeRepositoryBase(url, cache, GuestWelcomeRemoteSource { config.copy(enabled = false) })
        repo.refresh()
        assertEquals(GuestWelcomeContent.Local, repo.cachedContent())
        val cancelled = GuestWelcomeRepositoryBase(url, Cache(), GuestWelcomeRemoteSource { throw CancellationException("Disposed") })
        assertFailsWith<CancellationException> { cancelled.refresh() }
    }
}
