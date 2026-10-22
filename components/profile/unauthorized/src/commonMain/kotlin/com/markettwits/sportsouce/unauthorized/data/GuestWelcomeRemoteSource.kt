package com.markettwits.sportsouce.unauthorized.data

import com.markettwits.core_cloud.provider.defaultHttpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray

internal sealed interface GuestWelcomeRemoteResult {
    data class Updated(val config: GuestWelcomeConfig, val etag: String?, val lastModified: String?) : GuestWelcomeRemoteResult
    data class NotModified(val etag: String?, val lastModified: String?) : GuestWelcomeRemoteResult
}

internal fun interface GuestWelcomeRemoteSource {
    suspend fun fetch(url: String): GuestWelcomeConfig
    suspend fun revalidate(url: String, etag: String?, lastModified: String?): GuestWelcomeRemoteResult =
        GuestWelcomeRemoteResult.Updated(fetch(url), null, null)
}

/** A public-content client: it never uses the authenticated Sportsauce API client or its headers. */
internal class GuestWelcomeRemoteSourceBase(
    private val client: HttpClient = HttpClient(defaultHttpClientEngine()) {
        expectSuccess = true
        install(HttpTimeout) {
            requestTimeoutMillis = 6000
            connectTimeoutMillis = 4000
            socketTimeoutMillis = 4000
        }
    },
) : GuestWelcomeRemoteSource {
    override suspend fun fetch(url: String): GuestWelcomeConfig = when (val result = revalidate(url, null, null)) {
        is GuestWelcomeRemoteResult.Updated -> result.config
        is GuestWelcomeRemoteResult.NotModified -> error("Unexpected 304 without a cached welcome config")
    }

    override suspend fun revalidate(url: String, etag: String?, lastModified: String?): GuestWelcomeRemoteResult {
        require(isPublicHttpsUrl(url))
        val response = client.get(url) {
            // 304 is a valid revalidation result, not a redirect failure.
            expectSuccess = false
            header(HttpHeaders.Accept, "application/json, text/plain")
            if (!etag.isNullOrBlank()) header(HttpHeaders.IfNoneMatch, etag)
            if (!lastModified.isNullOrBlank()) header(HttpHeaders.IfModifiedSince, lastModified)
        }
        val newEtag = response.headers[HttpHeaders.ETag]
        val newModified = response.headers[HttpHeaders.LastModified]
        if (response.status == HttpStatusCode.NotModified) {
            response.bodyAsChannel().cancel(null)
            return GuestWelcomeRemoteResult.NotModified(newEtag, newModified)
        }
        if (response.status != HttpStatusCode.OK) {
            response.bodyAsChannel().cancel(null)
            error("Welcome config request failed: ${response.status.value}")
        }
        val channel = response.bodyAsChannel()
        val bytes = try { channel.readRemaining(65_537).readByteArray() } finally { channel.cancel(null) }
        require(bytes.size <= 65_536) { "Welcome config is too large" }
        return GuestWelcomeRemoteResult.Updated(parseGuestWelcomeConfig(bytes.decodeToString()), newEtag, newModified)
    }
}
