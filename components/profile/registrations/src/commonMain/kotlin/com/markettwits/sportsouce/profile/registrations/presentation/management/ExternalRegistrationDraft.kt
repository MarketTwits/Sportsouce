package com.markettwits.sportsouce.profile.registrations.presentation.management

import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.*

internal const val MaxPosterBytes = 5 * 1024 * 1024

internal data class RegistrationPoster(val name: String, val mimeType: String, val bytes: ByteArray, val lastModified: Long)
internal val posterMimeTypes = arrayOf("image/jpeg", "image/png", "image/webp")

internal fun checkedRegistrationPoster(name: String, bytes: ByteArray, lastModified: Long): RegistrationPoster {
    require(bytes.size <= MaxPosterBytes) { "Файл больше 5 МБ" }
    fun header(offset: Int, vararg signature: Int) =
        signature.indices.all { bytes.getOrNull(offset + it)?.toUByte()?.toInt() == signature[it] }

    val mimeType = when {
        header(0, 0xFF, 0xD8, 0xFF) -> "image/jpeg"
        header(0, 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A) -> "image/png"
        header(0, 0x52, 0x49, 0x46, 0x46) && header(8, 0x57, 0x45, 0x42, 0x50) -> "image/webp"
        else -> throw IllegalArgumentException("Выберите изображение JPG, PNG или WebP")
    }
    val safeName = name.map { if (it.isISOControl() || it == '"' || it == '\\') '_' else it }.joinToString("")
    return RegistrationPoster(safeName, mimeType, bytes, lastModified)
}

internal enum class ExternalRegistrationField {
    StartName, DistanceName, Date, Url,
}

internal data class ExternalRegistrationDraft(
    val id: Int?,
    val startName: String,
    val distanceName: String,
    val startDate: String,
    val url: String,
    val ageGroup: String,
    val team: String,
    val posterId: Int?,
    val poster: RegistrationPoster?,
) {
    fun errors(): Map<ExternalRegistrationField, String> = buildMap {
        if (startName.isBlank()) put(ExternalRegistrationField.StartName, "Введите название старта")
        if (distanceName.isBlank()) put(ExternalRegistrationField.DistanceName, "Введите дистанцию")
        when {
            startDate.isBlank() -> put(ExternalRegistrationField.Date, "Укажите дату старта")
            parseExternalRegistrationDate(startDate) == null ->
                put(ExternalRegistrationField.Date, "Введите существующую дату в формате ДД.ММ.ГГГГ")
        }
        when {
            url.isBlank() -> put(ExternalRegistrationField.Url, "Добавьте ссылку на страницу старта")
            externalRegistrationUrl(url) == null -> put(ExternalRegistrationField.Url, "Проверьте адрес страницы старта")
        }
    }

    fun body(uploadedPosterId: Int?): JsonObject = buildJsonObject {
        put("startName", startName.trim())
        put("distanceName", distanceName.trim())
        put("startDate", requireNotNull(parseExternalRegistrationDate(startDate)).toString())
        put("url", url.trim())
        put("ageGroup", ageGroup.trim())
        put("team", team.trim())
        put("posterId", uploadedPosterId?.let(::JsonPrimitive) ?: JsonNull)
    }
}

private val displayDatePattern = Regex("""(\d{1,2})\.(\d{1,2})\.(\d{4})""")
private val isoDatePattern = Regex("""\d{4}-\d{2}-\d{2}""")

internal fun parseExternalRegistrationDate(value: String): LocalDate? {
    val text = value.trim()
    val local = displayDatePattern.matchEntire(text)
    val iso = if (local != null) {
        val (_, day, month, year) = local.groupValues
        "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
    } else text.takeIf { isoDatePattern.matches(it) }
    return iso?.let(::parseDate)?.takeIf { it.year > 0 }
}

internal fun externalRegistrationUrl(value: String): String? {
    val text = value.trim()
    if (text.isEmpty() || text.any { it.isWhitespace() || it.isISOControl() || it == '\\' }) return null
    if (text.startsWith("//")) return null
    val slug = text.removePrefix("/").removePrefix("starts/")
    if (slug.any(Char::isLetterOrDigit) && slug.all { it.isLetterOrDigit() || it == '-' || it == '_' }) {
        return "https://sportsauce.ru/starts/$slug"
    }

    val hasScheme = text.startsWith("https://", ignoreCase = true) || text.startsWith("http://", ignoreCase = true)
    if (!hasScheme && "://" in text) return null
    val candidate = if (hasScheme) {
        "${text.substringBefore("://").lowercase()}://${text.substringAfter("://")}"
    } else "https://$text"
    val authority = candidate.substringAfter("://").takeWhile { it !in "/?#" }
    if (authority.isEmpty() || '@' in authority || '%' in authority) return null
    val url = runCatching { Url(candidate) }.getOrNull() ?: return null
    val host = url.host.removeSuffix(".")
    val labels = host.split('.')
    val validDomain = labels.size >= 2 && labels.all { label ->
        label.isNotEmpty() && label.first().isLetterOrDigit() && label.last().isLetterOrDigit() &&
            label.all { it.isLetterOrDigit() || it == '-' }
    }
    return candidate.takeIf { validDomain || (hasScheme && host.startsWith("[") && host.endsWith("]")) }
}
