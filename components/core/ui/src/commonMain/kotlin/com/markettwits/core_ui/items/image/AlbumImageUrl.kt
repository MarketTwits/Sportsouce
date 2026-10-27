package com.markettwits.core_ui.items.image

import io.ktor.http.Url
import io.ktor.http.decodeURLPart
import io.ktor.http.encodeURLParameter

/** Stable sizes keep CDN and Coil caches shared between albums, grids and the viewer. */
enum class AlbumImageSize(val pixels: Int, internal val quality: Int) {
    Preview(48, 40),
    Filmstrip(160, 65),
    Grid(480, 72),
    Cover(960, 78),
    Full(1600, 82),
}

/** Presentation-only transformation; repositories and navigation retain the original photo URL. */
fun albumImageUrl(original: String, size: AlbumImageSize): String {
    val source = albumOriginalUrl(original)
    val url = runCatching { Url(source) }.getOrNull() ?: return original
    if (url.protocol.name !in listOf("https", "http") || !url.host.equals("cdn.sportsauce.ru", true)) return original
    // Escape query strings, %, @, spaces and Unicode without hiding the readable scheme/path.
    val escaped = source.encodeURLParameter().replace("%3A", ":").replace("%2F", "/")
    return "https://img.sportsauce.ru/insecure/rs:fit:${size.pixels}:${size.pixels}/q:${size.quality}/plain/$escaped"
}

/** Resolve the independent CDN source for full quality and proxy failover. */
internal fun albumOriginalUrl(original: String): String {
    val url = runCatching { Url(original) }.getOrNull() ?: return original
    if (url.protocol.name !in listOf("https", "http")) return original
    return when {
        url.host.equals("cdn.sportsauce.ru", ignoreCase = true) -> original.substringBefore('#')
        url.host.equals("img.sportsauce.ru", ignoreCase = true) && "/plain/" in url.encodedPath -> {
            val nested = url.encodedPath.substringAfter("/plain/").substringBefore('@').decodeURLPart()
            val parsed = runCatching { Url(nested) }.getOrNull()
            if (parsed?.host?.equals("cdn.sportsauce.ru", ignoreCase = true) != true) return original
            nested
        }
        else -> return original
    }
}
