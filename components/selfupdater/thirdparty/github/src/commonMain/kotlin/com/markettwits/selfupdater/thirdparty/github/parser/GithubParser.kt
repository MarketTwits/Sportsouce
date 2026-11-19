package com.markettwits.selfupdater.thirdparty.github.parser

import com.markettwits.selfupdater.thirdparty.api.SelfUpdate
import com.markettwits.selfupdater.thirdparty.api.SelfUpdateParserApi
import com.markettwits.selfupdater.thirdparty.api.DesktopReleaseParserApi
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import com.markettwits.selfupdater.thirdparty.github.model.GithubRelease
import com.markettwits.selfupdater.thirdparty.github.model.DesktopReleaseManifest
import com.markettwits.selfupdater.thirdparty.github.model.desktopAsset
import com.markettwits.selfupdater.thirdparty.github.model.legacyDesktopAsset
import com.markettwits.selfupdater.models.ReleaseVersion
import com.markettwits.selfupdater.models.ReleaseNotesLocale
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

private const val GITHUB_API_LAST_RELEASE =
    "https://api.github.com/repos/MarketTwits/Sportsouce/releases/latest"

class GithubParser(
    private val client: HttpClient,
    private val language: () -> String = { ReleaseNotesLocale.Default },
) : SelfUpdateParserApi, DesktopReleaseParserApi {
    private val metadataMutex = Mutex()
    private var cachedRelease: GithubRelease? = null
    private var cachedEtag: String? = null
    private var rateLimitedUntil: TimeMark? = null
    private val notesMutex = Mutex()
    private val notesCache = mutableMapOf<NotesKey, String>()

    override fun getName(): String = "GitHub"

    override suspend fun getLastUpdate(): SelfUpdate? {
        val update = parseReleaseUpdate()
        val asset = update.androidAsset()

        val version = requireNotNull(ReleaseVersion.fromTag(update.tagName))
        return SelfUpdate(
            version = update.tagName,
            downloadUrl = asset.browserDownloadUrl,
            name = asset.name,
            description = getReleaseNotes(update, version.versionName),
            sizeBytes = asset.size,
            sha256 = asset.sha256OrNull(),
        )
    }

    override suspend fun getLatestDesktopUpdate(platform: DesktopPlatform, architecture: DesktopArchitecture): SelfUpdate? {
        val release = parseReleaseUpdate()
        require(!release.draft && !release.preRelease && ReleaseVersion.fromTag(release.tagName) != null) { "Invalid stable release" }
        val manifestAssets = release.assets.filter { it.name == "update-manifest.json" && it.state == "uploaded" }
        if (manifestAssets.isEmpty()) {
            val asset = release.legacyDesktopAsset(platform, architecture) ?: return null
            val version = requireNotNull(ReleaseVersion.fromTag(release.tagName))
            return SelfUpdate(release.tagName, asset.browserDownloadUrl, asset.name,
                getReleaseNotes(release, version.versionName), asset.size, asset.sha256OrNull())
        }
        require(manifestAssets.size == 1) { "Ambiguous release manifest" }
        val manifestAsset = manifestAssets.single()
        require(manifestAsset.size in 1..MAX_MANIFEST_BYTES && manifestAsset.browserDownloadUrl ==
            "https://github.com/MarketTwits/Sportsouce/releases/download/${release.tagName}/update-manifest.json") { "Invalid manifest asset" }
        val manifestText = client.prepareGet(manifestAsset.browserDownloadUrl).execute { response ->
            requireNotNull(readBoundedText(response, MAX_MANIFEST_BYTES)) { "Desktop manifest exceeds size limit" }
        }
        val manifest = JsonProviderBase().provide().decodeFromString<DesktopReleaseManifest>(manifestText)
        val asset = release.desktopAsset(manifest, platform, architecture) ?: return null
        return SelfUpdate(
            version = release.tagName,
            downloadUrl = asset.browserDownloadUrl,
            name = asset.name,
            description = getReleaseNotes(release, manifest.versionName),
            sizeBytes = asset.size,
            sha256 = asset.sha256OrNull(),
        )
    }

    private suspend fun getReleaseNotes(update: GithubRelease, versionName: String): String {
        val locale = language()
        val notes = withTimeoutOrNull(NOTES_TOTAL_TIMEOUT_MS) { getLocalizedReleaseNotes(update, locale) }
        // The public GitHub body is English editorial content, never a Russian UI fallback.
        val publicBody = if (ReleaseNotesLocale.isRussian(locale)) null
            else update.description?.takeIf { it.isNotBlank() && it.length <= MAX_NOTES_CHARS }
        return (notes ?: publicBody ?: ReleaseNotesLocale.unavailable(locale)).replace("\$APP_VERSION", versionName)
    }

    private suspend fun getLocalizedReleaseNotes(update: GithubRelease, language: String): String? {
        val locales = listOf(if (ReleaseNotesLocale.isRussian(language)) "ru-RU" else "en-US")
        for (locale in locales) {
            val name = "whatsnew-$locale.txt"
            val expectedUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/${update.tagName}/$name"
            val assets = update.assets.filter { it.name == name }
            val asset = assets.singleOrNull()?.takeIf {
                it.state == "uploaded" && it.size in 1..MAX_NOTES_BYTES && it.browserDownloadUrl == expectedUrl
            }
            val key = NotesKey(update.tagName, locale, asset?.browserDownloadUrl, asset?.size, asset?.digest)
            val text = notesMutex.withLock {
                notesCache[key] ?: run {
                    // Do not cache failed requests or a fallback from one locale as another locale.
                    val raw = "https://raw.githubusercontent.com/MarketTwits/Sportsouce/${update.tagName}/.github/whatsnew/whatsnew-$locale"
                    val snapshot = asset?.browserDownloadUrl?.let { getBoundedNotes(it) }
                    val result = snapshot ?: getBoundedNotes(raw)
                    if (result != null && (asset == null || snapshot != null)) {
                        if (notesCache.size >= MAX_CACHED_NOTES) notesCache.remove(notesCache.keys.first())
                        notesCache[key] = result
                    }
                    result
                }
            }
            if (text != null) return text
        }
        return null
    }

    private suspend fun getBoundedNotes(url: String): String? = try {
        withTimeoutOrNull(NOTES_TIMEOUT_MS) {
            client.prepareGet(url).execute { response ->
                readBoundedText(response, MAX_NOTES_BYTES)
                    ?.takeIf { it.isNotBlank() && it.length <= MAX_NOTES_CHARS }
            }
        }
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (_: Exception) {
        null
    }

    private suspend fun readBoundedText(response: HttpResponse, maxBytes: Long): String? {
        if (response.status != HttpStatusCode.OK) return null
        val length = response.headers[HttpHeaders.ContentLength]?.toLongOrNull()
        if (length != null && length > maxBytes) return null
        val channel = response.bodyAsChannel()
        val buffer = ByteArray(maxBytes.toInt() + 1)
        var size = 0
        while (size < buffer.size) {
            val count = channel.readAvailable(buffer, size, buffer.size - size)
            if (count < 0) break
            size += count
        }
        if (size > maxBytes) return null
        return buffer.decodeToString(0, size, throwOnInvalidSequence = true)
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun parseReleaseUpdate(): GithubRelease = metadataMutex.withLock {
        if (rateLimitedUntil?.hasNotPassedNow() == true) throw GithubMetadataException("GitHub update checks are temporarily rate limited")
        val response = client.get(GITHUB_API_LAST_RELEASE) {
            expectSuccess = false
            cachedEtag?.let { header(HttpHeaders.IfNoneMatch, it) }
        }
        when (response.status) {
            HttpStatusCode.NotModified -> cachedRelease ?: throw GithubMetadataException("GitHub returned 304 without cached release metadata")
            HttpStatusCode.OK -> {
                val release: GithubRelease = response.body()
                cachedRelease = release
                cachedEtag = response.headers[HttpHeaders.ETag]?.takeIf { it.isNotBlank() && it.length <= 1024 }
                rateLimitedUntil = null
                release
            }
            else -> {
                val retryAfter = response.headers[HttpHeaders.RetryAfter]?.toLongOrNull()
                val limited = response.status == HttpStatusCode.TooManyRequests ||
                    (response.status == HttpStatusCode.Forbidden &&
                        (response.headers["X-RateLimit-Remaining"] == "0" || retryAfter != null))
                if (limited) {
                    val now = Clock.System.now().epochSeconds
                    val reset = response.headers["X-RateLimit-Reset"]?.toLongOrNull()
                    val untilReset = reset?.let { if (it <= now) 1 else it - now }
                    val seconds = (retryAfter ?: untilReset ?: 60).coerceIn(1, 3600)
                    rateLimitedUntil = TimeSource.Monotonic.markNow() + seconds.seconds
                }
                throw GithubMetadataException("GitHub update check failed (HTTP ${response.status.value})")
            }
        }
    }

}

private const val MAX_NOTES_CHARS = 32_768
private const val MAX_NOTES_BYTES = 131_072L
private const val NOTES_TIMEOUT_MS = 5_000L
private const val NOTES_TOTAL_TIMEOUT_MS = 8_000L
private const val MAX_MANIFEST_BYTES = 131_072L
private const val MAX_CACHED_NOTES = 8

private data class NotesKey(val tag: String, val locale: String, val url: String?, val size: Long?, val digest: String?)

internal class GithubMetadataException(message: String) : IllegalStateException(message)
