package com.markettwits.selfupdater.thirdparty.github

import com.markettwits.selfupdater.thirdparty.github.model.GithubAsset
import com.markettwits.selfupdater.thirdparty.github.model.GithubRelease
import com.markettwits.selfupdater.thirdparty.github.di.configureGithubClient
import com.markettwits.selfupdater.thirdparty.github.parser.GithubParser
import com.markettwits.selfupdater.models.ReleaseNotesLocale
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GithubParserTest {
    @Test
    fun defaultAppLanguageUsesRussianFromTheExactReleaseEvenWhenGithubBodyIsEnglish() = runTest {
        val urls = mutableListOf<String>()
        val client = HttpClient(testEngine { request ->
            urls += request.url.toString()
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","body":"English public GitHub release","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else respond("Русские изменения для \$APP_VERSION", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
        }) { configureGithubClient() }
        try {
            assertEquals("Русские изменения для 1.6.1", GithubParser(client).getLastUpdate()?.description)
            assertEquals("https://raw.githubusercontent.com/MarketTwits/Sportsouce/1.6.1.10422/.github/whatsnew/whatsnew-ru-RU", urls.last())
            assertTrue(urls.none { it.contains("/dev/") || it.contains("whatsnew-en-US") })
        } finally { client.close() }
    }

    @Test
    fun slowReleaseNotesDoNotHoldUpAnAvailableAndroidUpdate() = runTest {
        val client = HttpClient(testEngine { request ->
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","body":"Release ${'$'}APP_VERSION","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else {
                delay(30_000)
                respond("late")
            }
        }) { configureGithubClient() }
        try {
            val update = assertNotNull(GithubParser(client) { "ru" }.getLastUpdate())
            assertEquals(ReleaseNotesLocale.unavailable(), update.description)
            assertEquals(apk.browserDownloadUrl, update.downloadUrl)
            assertEquals(5_000, testScheduler.currentTime)
        } finally { client.close() }
    }
    @Test
    fun errorResponseIsNotDisplayedOrCachedAsReleaseNotesWhenClientAllowsErrors() = runTest {
        var notesHealthy = false
        val client = HttpClient(testEngine { request ->
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","body":"Fallback notes","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else if (notesHealthy) respond("Recovered notes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
            else respond("Server error page", status = HttpStatusCode.ServiceUnavailable)
        }) { configureGithubClient(); expectSuccess = false }
        try {
            val parser = GithubParser(client) { "en" }
            assertEquals("Fallback notes", parser.getLastUpdate()?.description)
            notesHealthy = true
            assertEquals("Recovered notes", parser.getLastUpdate()?.description)
        } finally { client.close() }
    }

    @Test
    fun inspectedLegacyReleaseCanBeUsedWithoutManifest() = runTest {
        val name = "Sportsauce-1.6.1-10422.dmg"
        val url = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/$name"
        val digest = "718a5a287933f627e11ffb8a421692ac4dfd498ffbc7477ed0a5377f9114a49f"
        val client = HttpClient(testEngine { request ->
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","assets":[{"name":"$name","browser_download_url":"$url","state":"uploaded","size":80903862,"digest":"sha256:$digest"}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else respond("Release notes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
        }) { configureGithubClient() }
        try {
            val parser = GithubParser(client) { "en" }
            val update = assertNotNull(parser.getLatestDesktopUpdate(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
            assertEquals(url, update.downloadUrl)
            assertEquals(digest, update.sha256)
            assertEquals(null, parser.getLatestDesktopUpdate(DesktopPlatform.MacOS, DesktopArchitecture.X64))
        } finally { client.close() }
    }
    @Test
    fun malformedOrOversizedDesktopManifestFailsBeforeChoosingAnInstaller() = runTest {
        val manifestUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/update-manifest.json"
        for (content in listOf("{broken", "x".repeat(131_073))) {
            val urls = mutableListOf<String>()
            val client = HttpClient(testEngine { request ->
                urls += request.url.toString()
                if (request.url.host == "api.github.com") respond(
                    """{"tag_name":"1.6.1.10422","assets":[{"name":"update-manifest.json","browser_download_url":"$manifestUrl","state":"uploaded","size":512}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                ) else respond(content, headers = headersOf(HttpHeaders.ContentType, "application/json"))
            }) { configureGithubClient() }
            try {
                assertFailsWith<Exception> { GithubParser(client).getLatestDesktopUpdate(DesktopPlatform.MacOS, DesktopArchitecture.Arm64) }
                assertEquals(listOf("https://api.github.com/repos/MarketTwits/Sportsouce/releases/latest", manifestUrl), urls)
            } finally { client.close() }
        }
    }
    @Test
    fun notesAreCachedByReleaseAndLanguageButFailedPrimaryLocaleIsRetried() = runTest {
        var language = "ru-RU"
        var ruAvailable = false
        var ruRequests = 0
        var enRequests = 0
        val client = HttpClient(testEngine { request ->
            when {
                request.url.host == "api.github.com" -> respond(
                    """{"tag_name":"1.6.1.10422","body":"fallback","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
                request.url.toString().endsWith("whatsnew-ru-RU") -> {
                    ruRequests++
                    if (ruAvailable) respond("Русский \$APP_VERSION", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
                    else respond("unavailable", status = HttpStatusCode.ServiceUnavailable)
                }
                else -> {
                    enRequests++
                    respond("English \$APP_VERSION", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
                }
            }
        }) { configureGithubClient() }
        try {
            val parser = GithubParser(client) { language }
            assertEquals(ReleaseNotesLocale.unavailable(), parser.getLastUpdate()?.description)
            ruAvailable = true
            assertEquals("Русский 1.6.1", parser.getLastUpdate()?.description)
            assertEquals("Русский 1.6.1", parser.getLastUpdate()?.description)
            language = "en-US"
            assertEquals("English 1.6.1", parser.getLastUpdate()?.description)
            assertEquals(2, ruRequests)
            assertEquals(1, enRequests)
        } finally { client.close() }
    }

    @Test
    fun localizedReleaseSnapshotTakesPrecedenceOverHistoricalRawNotes() = runTest {
        val urls = mutableListOf<String>()
        val snapshot = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/whatsnew-ru-RU.txt"
        val client = HttpClient(testEngine { request ->
            val url = request.url.toString()
            urls += url
            when (request.url.host) {
                "api.github.com" -> respond(
                    """{"tag_name":"1.6.1.10422","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1},{"name":"whatsnew-ru-RU.txt","browser_download_url":"$snapshot","state":"uploaded","size":20}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
                "github.com" -> respond("Снимок 1.6.1", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
                else -> respond("old notes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
            }
        }) { configureGithubClient() }
        try {
            assertEquals("Снимок 1.6.1", GithubParser(client) { "ru" }.getLastUpdate()?.description)
            assertTrue(snapshot in urls)
            assertTrue(urls.none { it.contains("raw.githubusercontent.com") })
        } finally { client.close() }
    }

    @Test
    fun untrustedSnapshotUrlIsIgnoredAndFallsBackToTag() = runTest {
        val urls = mutableListOf<String>()
        val client = HttpClient(testEngine { request ->
            urls += request.url.toString()
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1},{"name":"whatsnew-en-US.txt","browser_download_url":"https://evil.example/notes","state":"uploaded","size":20}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else respond("tag notes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
        }) { configureGithubClient() }
        try {
            assertEquals("tag notes", GithubParser(client) { "en" }.getLastUpdate()?.description)
            assertTrue(urls.none { it.contains("evil.example") })
            assertTrue(urls.any { it.contains("raw.githubusercontent.com") })
        } finally { client.close() }
    }
    @Test
    fun conditionalReleaseRequestsReuseOnlyPreviouslyReceivedMetadata() = runTest {
        var requests = 0
        val client = HttpClient(testEngine { request ->
            if (request.url.host != "api.github.com") {
                respond("notes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
            } else {
                requests++
                if (requests == 1) {
                    assertEquals(null, request.headers[HttpHeaders.IfNoneMatch])
                    respond(
                        """{"tag_name":"1.6.1.10422","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                        headers = headersOf(HttpHeaders.ContentType to listOf("application/json"), HttpHeaders.ETag to listOf("\"release-v1\"")),
                    )
                } else {
                    assertEquals("\"release-v1\"", request.headers[HttpHeaders.IfNoneMatch])
                    respond("", status = HttpStatusCode.NotModified)
                }
            }
        }) { configureGithubClient() }
        try {
            val parser = GithubParser(client) { "en" }
            assertEquals("1.6.1.10422", parser.getLastUpdate()?.version)
            assertEquals("1.6.1.10422", parser.getLastUpdate()?.version)
            assertEquals(2, requests)
        } finally { client.close() }
    }

    @Test
    fun rateLimitDoesNotCauseRepeatedRequestsOrPretendThereIsNoUpdate() = runTest {
        var requests = 0
        val client = HttpClient(testEngine {
            requests++
            respond("rate limited", status = HttpStatusCode.TooManyRequests, headers = headersOf(HttpHeaders.RetryAfter, "120"))
        }) { configureGithubClient() }
        try {
            val parser = GithubParser(client) { "en" }
            assertFailsWith<IllegalStateException> { parser.getLastUpdate() }
            assertFailsWith<IllegalStateException> { parser.getLastUpdate() }
            assertEquals(1, requests)
        } finally { client.close() }
    }

    @Test
    fun forbiddenWithRetryAfterAlsoSuppressesImmediateRetries() = runTest {
        var requests = 0
        val client = HttpClient(testEngine {
            requests++
            respond("secondary rate limit", status = HttpStatusCode.Forbidden, headers = headersOf(HttpHeaders.RetryAfter, "60"))
        }) { configureGithubClient() }
        try {
            val parser = GithubParser(client) { "en" }
            assertFailsWith<IllegalStateException> { parser.getLastUpdate() }
            assertFailsWith<IllegalStateException> { parser.getLastUpdate() }
            assertEquals(1, requests)
        } finally { client.close() }
    }

    @Test
    fun notModifiedWithoutCachedDataIsAnError() = runTest {
        val client = HttpClient(testEngine { respond("", status = HttpStatusCode.NotModified) }) { configureGithubClient() }
        try {
            val failure = assertFailsWith<IllegalStateException> { GithubParser(client).getLastUpdate() }
            assertTrue(failure.message.orEmpty().contains("without cached"))
        } finally { client.close() }
    }
    private fun TestScope.testEngine(handler: MockRequestHandler): MockEngine = MockEngine(
        MockEngineConfig().apply {
            dispatcher = StandardTestDispatcher(testScheduler)
            addHandler(handler)
        }
    )
    private val apk = GithubAsset(
        name = "Sportsauce-1.6.1-10422.apk",
        browserDownloadUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/Sportsauce-1.6.1-10422.apk",
        state = "uploaded",
        size = 8892395,
    )

    @Test
    fun desktopRequiresExplicitManifestAndMatchesReleaseAsset() = runTest {
        val hash = "a".repeat(64)
        val manifestUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/update-manifest.json"
        val dmgName = "Sportsauce-1.6.1-10422.dmg"
        val dmgUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/$dmgName"
        var includeManifest = true
        val requests = mutableListOf<String>()
        val client = HttpClient(testEngine { request ->
            val url = request.url.toString()
            requests += url
            when {
                request.url.host == "api.github.com" -> {
                    val manifestAsset = if (includeManifest) """,{"name":"update-manifest.json","browser_download_url":"$manifestUrl","state":"uploaded","size":512}""" else ""
                    respond("""{"tag_name":"1.6.1.10422","body":"release","assets":[{"name":"$dmgName","browser_download_url":"$dmgUrl","state":"uploaded","size":123,"digest":"sha256:$hash"}$manifestAsset]}""", headers = headersOf(HttpHeaders.ContentType, "application/json"))
                }
                url == manifestUrl -> respond("""{"schemaVersion":1,"releaseId":"1.6.1.10422","versionName":"1.6.1","buildNumber":10422,"channel":"stable","assets":[{"platform":"macos","architecture":"arm64","packageType":"dmg","name":"$dmgName","size":123,"sha256":"$hash"}]}""", headers = headersOf(HttpHeaders.ContentType, "application/json"))
                else -> respond("changes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
            }
        }) { configureGithubClient() }
        try {
            val parser = GithubParser(client) { "en" }
            val update = assertNotNull(parser.getLatestDesktopUpdate(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
            assertEquals(dmgUrl, update.downloadUrl)
            assertEquals(hash, update.sha256)
            assertEquals(null, parser.getLatestDesktopUpdate(DesktopPlatform.MacOS, DesktopArchitecture.X64))
            includeManifest = false
            assertEquals(null, parser.getLatestDesktopUpdate(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
            assertEquals(true, requests.contains(manifestUrl))
        } finally { client.close() }
    }

    @Test
    fun fetchesCurrentRepositoryAndSelectsPublishedApk() = runTest {
        val requestedUrls = mutableListOf<String>()
        var accept = ""
        val client = HttpClient(testEngine { request ->
            requestedUrls += request.url.toString()
            accept = request.headers[HttpHeaders.Accept].orEmpty()
            if (request.url.host == "raw.githubusercontent.com") {
                return@testEngine respond("Исправления для \$APP_VERSION", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
            }
            respond(
                content = """{
                  "tag_name":"1.6.1.10422", "name":null, "body":null,
                  "draft":false, "prerelease":false, "new_field":true,
                  "assets":[
                    {"name":"Sportsauce-1.6.1-10422.dmg","browser_download_url":"https://example.com/a.dmg","state":"uploaded","size":10},
                    {"name":"Sportsauce-1.6.1-10422.apk","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":8892395,"digest":"sha256:${"a".repeat(64)}"}
                  ]
                }""".trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }) {
            configureGithubClient()
        }
        try {
            val update = assertNotNull(GithubParser(client) { "ru-RU" }.getLastUpdate())
            assertEquals("https://api.github.com/repos/MarketTwits/Sportsouce/releases/latest", requestedUrls.first())
            assertEquals("https://raw.githubusercontent.com/MarketTwits/Sportsouce/1.6.1.10422/.github/whatsnew/whatsnew-ru-RU", requestedUrls.last())
            assertEquals("application/vnd.github+json", accept)
            assertEquals("1.6.1.10422", update.version)
            assertEquals(apk.name, update.name)
            assertEquals(apk.browserDownloadUrl, update.downloadUrl)
            assertEquals("Исправления для 1.6.1", update.description)
            assertEquals(8892395, update.sizeBytes)
            assertEquals("a".repeat(64), update.sha256)
        } finally {
            client.close()
        }
    }

    @Test
    fun missingRussianNotesNeverFallBackToEnglishOrPublicGithubBody() = runTest {
        val paths = mutableListOf<String>()
        val client = HttpClient(testEngine { request ->
            paths += request.url.toString()
            if (request.url.host == "api.github.com") {
                respond(
                    """{"tag_name":"1.6.1.10422","body":"Release notes","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            } else respond("missing", status = HttpStatusCode.NotFound)
        }) { configureGithubClient() }
        try {
            val update = assertNotNull(GithubParser(client) { "ru" }.getLastUpdate())
            assertEquals(ReleaseNotesLocale.unavailable(), update.description)
            assertEquals(2, paths.size)
            assertEquals(true, paths[1].endsWith("whatsnew-ru-RU"))
            assertTrue(paths.none { it.contains("whatsnew-en-US") || it.contains("/dev/") })
            assertEquals(null, update.sha256)
        } finally {
            client.close()
        }
    }

    @Test
    fun missingNotesAndReleaseBodyUseNeutralFallback() = runTest {
        val client = HttpClient(testEngine { request ->
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","body":null,"assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else respond("missing", status = HttpStatusCode.NotFound)
        }) { configureGithubClient() }
        try {
            assertEquals(ReleaseNotesLocale.unavailable(), GithubParser(client) { "ru" }.getLastUpdate()?.description)
        } finally { client.close() }
    }

    @Test
    fun oversizedNotesWithoutContentLengthFallBackToReleaseBody() = runTest {
        val client = HttpClient(testEngine { request ->
            if (request.url.host == "api.github.com") respond(
                """{"tag_name":"1.6.1.10422","body":"Safe release body","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            ) else respond("x".repeat(131_073), headers = headersOf(HttpHeaders.ContentType, "text/plain"))
        }) { configureGithubClient() }
        try {
            assertEquals("Safe release body", GithubParser(client) { "en" }.getLastUpdate()?.description)
        } finally { client.close() }
    }

    @Test
    fun validatesOptionalAssetDigest() {
        assertEquals("b".repeat(64), apk.copy(digest = "sha256:${"b".repeat(64)}").sha256OrNull())
        listOf("b".repeat(64), "sha256:abc", "sha256:${"g".repeat(64)}", "sha1:${"b".repeat(64)}")
            .forEach { assertEquals(null, apk.copy(digest = it).sha256OrNull()) }
    }

    @Test
    fun cancellationWhileLoadingNotesIsNotConvertedToFallback() = runTest {
        val client = HttpClient(testEngine { request ->
            if (request.url.host == "api.github.com") {
                respond(
                    """{"tag_name":"1.6.1.10422","body":"Release notes","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            } else throw CancellationException("cancelled")
        }) { configureGithubClient() }
        try {
            assertFailsWith<CancellationException> { GithubParser(client) { "ru" }.getLastUpdate() }
        } finally {
            client.close()
        }
    }

    @Test
    fun slowLocalizedNotesFallBackWithoutBlockingUpdateAvailability() = runTest {
        val client = HttpClient(testEngine { request ->
            when {
                request.url.host == "api.github.com" -> respond(
                    """{"tag_name":"1.6.1.10422","body":"Release notes","assets":[{"name":"${apk.name}","browser_download_url":"${apk.browserDownloadUrl}","state":"uploaded","size":1}]}""",
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
                request.url.toString().endsWith("whatsnew-ru-RU") -> {
                    delay(10_000)
                    respond("late")
                }
                else -> respond("English notes", headers = headersOf(HttpHeaders.ContentType, "text/plain"))
            }
        }) { configureGithubClient() }
        try {
            assertEquals(ReleaseNotesLocale.unavailable(), GithubParser(client) { "ru" }.getLastUpdate()?.description)
        } finally {
            client.close()
        }
    }

    @Test
    fun rejectsIncompleteAmbiguousAndUntrustedReleases() {
        fun release(assets: List<GithubAsset> = listOf(apk), tag: String = "1.6.1.10422", draft: Boolean = false, prerelease: Boolean = false) =
            GithubRelease(tagName = tag, assets = assets, draft = draft, preRelease = prerelease)

        listOf(
            release(emptyList()),
            release(listOf(apk, apk)),
            release(listOf(apk.copy(state = "new"))),
            release(listOf(apk.copy(size = 0))),
            release(listOf(apk.copy(browserDownloadUrl = "https://attacker.example/app.apk"))),
            release(tag = "1.6.1"),
            release(draft = true),
            release(prerelease = true),
        ).forEach { assertFailsWith<IllegalArgumentException> { it.androidAsset() } }
    }
}
