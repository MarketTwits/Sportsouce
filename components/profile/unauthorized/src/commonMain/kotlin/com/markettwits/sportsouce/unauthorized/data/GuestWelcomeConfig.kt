package com.markettwits.sportsouce.unauthorized.data

import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class GuestWelcomeConfig(
    val schemaVersion: Int,
    val enabled: Boolean = true,
    val variants: List<GuestWelcomeVariant> = emptyList(),
) {
    fun contents(): List<GuestWelcomeContent> {
        require(schemaVersion == 1) { "Unsupported welcome config version" }
        require(variants.size <= 30) { "Too many welcome variants" }
        if (!enabled) return emptyList()
        val active = variants.filter { it.enabled }
        require(active.map { it.id }.distinct().size == active.size) { "Duplicate welcome IDs" }
        return active.map { variant ->
            require(variant.id.isNotBlank() && variant.id.length <= 64)
            require(variant.headingLines.size in 1..3)
            require(variant.headingLines.all { validText(it, 28) })
            require(validText(variant.panelTitle, 64) && validText(variant.description, 180))
            require(isPublicHttpsUrl(variant.imageUrl))
            require(variant.imageAlignmentX.isFinite() && variant.imageAlignmentX in -1f..1f)
            require(variant.imageAlignmentY.isFinite() && variant.imageAlignmentY in -1f..1f)
            GuestWelcomeContent(variant.id, variant.headingLines.map(String::trim), variant.panelTitle.trim(),
                variant.description.trim(), variant.imageUrl, variant.imageAlignmentX, variant.imageAlignmentY)
        }
    }
}

@Serializable
internal data class GuestWelcomeVariant(
    val id: String,
    val enabled: Boolean = true,
    val headingLines: List<String>,
    val panelTitle: String,
    val description: String,
    val imageUrl: String,
    val imageAlignmentX: Float = .25f,
    val imageAlignmentY: Float = 0f,
)

internal val GuestWelcomeJson = Json { ignoreUnknownKeys = true }
internal fun parseGuestWelcomeConfig(raw: String): GuestWelcomeConfig {
    require(raw.length <= 65_536) { "Welcome config is too large" }
    return GuestWelcomeJson.decodeFromString<GuestWelcomeConfig>(raw).also { it.contents() }
}

private fun validText(value: String, limit: Int) = value.isNotBlank() && value.length <= limit &&
    value.none { it == '\n' || it == '\r' || it.code < 32 }

internal fun isPublicHttpsUrl(value: String): Boolean = runCatching {
    val url = Url(value)
    url.protocol == URLProtocol.HTTPS && url.host.isNotBlank() && url.user.isNullOrEmpty() &&
        url.password.isNullOrEmpty() && url.fragment.isEmpty()
}.getOrDefault(false)
