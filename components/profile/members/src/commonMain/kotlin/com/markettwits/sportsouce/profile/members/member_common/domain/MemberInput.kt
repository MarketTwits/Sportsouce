package com.markettwits.sportsouce.profile.members.member_common.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal fun memberBirthDate(value: String): LocalDate {
    val input = value.trim()
    return if ('T' in input) Instant.parse(input).toLocalDateTime(TimeZone.of("Asia/Novosibirsk")).date
    else if ('.' in input) {
        val parts = input.split('.')
        require(parts.size == 3) { "Введите дату рождения в формате ДД.ММ.ГГГГ" }
        LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
    } else LocalDate.parse(input)
}

/** API expects digits, while the phone field displays the human-readable RU mask. */
internal fun normalizeMemberPhone(value: String): String {
    val digits = value.filter(Char::isDigit)
    return when {
        digits.length == 10 -> "7$digits"
        digits.length == 11 && digits.startsWith("8") -> "7${digits.drop(1)}"
        else -> digits
    }
}
