package com.markettwits.sportsouce.profile.results.domain

import kotlin.math.roundToLong

fun resultTime(seconds: Double?): String {
    if (seconds == null || !seconds.isFinite() || seconds < 0 || seconds > 315_360_000) return "—"
    val value = seconds.roundToLong()
    val hours = value / 3600
    val minutes = (value % 3600) / 60
    val remainder = (value % 60).toString().padStart(2, '0')
    return if (hours > 0) "$hours:${minutes.toString().padStart(2, '0')}:$remainder" else "$minutes:$remainder"
}

fun resultDate(date: String?): String {
    if (date.isNullOrBlank()) return "Дата не указана"
    val parts = date.take(10).split('-')
    val month = parts.getOrNull(1)?.toIntOrNull()
    val day = parts.getOrNull(2)?.toIntOrNull()
    val months = listOf("янв", "фев", "мар", "апр", "мая", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
    return if (parts.size == 3 && month in 1..12 && day in 1..31) "$day ${months[month!! - 1]} ${parts[0]}" else date
}

fun MyResult.resultLabel(): String = when {
    kind == "upcoming" -> "Старт впереди"
    kind == "awaiting" -> "Результаты ещё не опубликованы"
    kind == "file" -> "Протокол в файле"
    status != null && status.lowercase() !in setOf("finished", "finish", "ok") -> status.uppercase()
    resultSeconds != null -> resultTime(resultSeconds)
    !resultText.isNullOrBlank() -> resultText
    else -> "Время не указано"
}

fun resultPlace(place: Int?, total: Int?): String? = place?.takeIf { it > 0 }?.let {
    "$it место" + if (total != null && total >= it) " из $total" else ""
}

internal fun resultCountWord(count: Int, one: String, few: String, many: String): String = when {
    count % 100 in 11..14 -> many
    count % 10 == 1 -> one
    count % 10 in 2..4 -> few
    else -> many
}
