package com.markettwits.sportsouce.club.registration.domain

/** Profile autofill and the phone field use different formatting for the same number. */
internal fun normalizeClubPhone(phone: String): String? {
    val value = phone.trim()
    if (!Regex("^[+0-9()\\s-]+$").matches(value)) return null
    val digits = value.filter { it in '0'..'9' }
    return when {
        digits.length == 11 && digits.first() in "78" -> "7" + digits.drop(1)
        digits.length == 10 && digits.first() == '9' && '+' !in value -> "7$digits"
        else -> null
    }
}
