package com.markettwits.core.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/** Formats an inclusive date range using the same UTC dates as [TimeMapper.mapTime]. */
fun TimeMapper.mapDateRange(start: String, end: String? = null): String {
    val first = start.toUtcDateOrNull() ?: return ""
    val last = end?.toUtcDateOrNull()?.takeIf { it > first } ?: first
    val firstLabel = mapTime(TimePattern.FullWithEmptySpace, first.toString())
    if (first == last) return firstLabel

    val lastLabel = mapTime(TimePattern.FullWithEmptySpace, last.toString())
    return when {
        first.year != last.year -> "$firstLabel — $lastLabel"
        first.month != last.month -> "${firstLabel.removeSuffix(" ${first.year}")} — $lastLabel"
        else -> "${first.day}–$lastLabel"
    }
}

private fun String.toUtcDateOrNull(): LocalDate? = runCatching {
    val value = trim()
    if (value.length == 10) LocalDate.parse(value)
    else Instant.parse(value).toLocalDateTime(TimeZone.UTC).date
}.getOrNull()
