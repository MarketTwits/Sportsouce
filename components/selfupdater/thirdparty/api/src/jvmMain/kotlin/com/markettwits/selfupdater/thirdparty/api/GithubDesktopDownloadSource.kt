package com.markettwits.selfupdater.thirdparty.api

import java.io.FilterInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI

/** Opens GitHub release assets without forwarding credentials to arbitrary redirects. */
class GithubDesktopDownloadSource(private val assetUrl: String) : DesktopDownloadSource {
    override fun open(): InputStream {
        var url = URI(assetUrl)
        require(isAllowedReleaseUrl(url)) { "Untrusted release URL" }
        repeat(MAX_REDIRECTS + 1) { redirectCount ->
            val connection = url.toURL().openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.setRequestProperty("Accept", "application/octet-stream")
            try {
                val status = connection.responseCode
                if (status in REDIRECT_CODES) {
                    check(redirectCount < MAX_REDIRECTS) { "Too many download redirects" }
                    val location = connection.getHeaderField("Location") ?: error("Missing redirect location")
                    val next = url.resolve(location)
                    check(isAllowedDownloadUrl(next) && (!next.host.equals("github.com", ignoreCase = true) || isAllowedReleaseUrl(next))) { "Untrusted download redirect" }
                    connection.disconnect()
                    url = next
                } else {
                    check(status == HttpURLConnection.HTTP_OK) { "Download failed with HTTP $status" }
                    return object : FilterInputStream(connection.inputStream) {
                        override fun close() {
                            try { super.close() } finally { connection.disconnect() }
                        }
                    }
                }
            } catch (failure: Exception) {
                connection.disconnect()
                throw failure
            }
        }
        error("Too many download redirects")
    }

    private companion object {
        const val MAX_REDIRECTS = 5
        val REDIRECT_CODES = setOf(301, 302, 303, 307, 308)
    }
}

internal fun isAllowedReleaseUrl(url: URI): Boolean {
    if (!isAllowedDownloadUrl(url) || !url.host.equals("github.com", ignoreCase = true) || url.rawQuery != null) return false
    val match = Regex("/MarketTwits/Sportsouce/releases/download/v?([0-9]+\\.[0-9]+\\.[0-9]+)\\.([0-9]+)/Sportsauce-([0-9]+\\.[0-9]+\\.[0-9]+)-([0-9]+)\\.(exe|dmg)")
        .matchEntire(url.rawPath) ?: return false
    return match.groupValues[1] == match.groupValues[3] && match.groupValues[2] == match.groupValues[4]
}

internal fun isAllowedDownloadUrl(url: URI): Boolean =
    url.scheme.equals("https", ignoreCase = true) && url.userInfo == null &&
        (url.port == -1 || url.port == 443) && url.rawFragment == null &&
        url.host?.lowercase() in setOf("github.com", "release-assets.githubusercontent.com", "objects.githubusercontent.com")
