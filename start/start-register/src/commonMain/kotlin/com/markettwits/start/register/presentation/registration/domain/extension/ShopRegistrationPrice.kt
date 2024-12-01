package com.markettwits.start.register.presentation.registration.domain.extension

import java.text.NumberFormat
import java.util.Locale

internal fun Int.formatPrice(): String {
    val formatter = NumberFormat.getInstance(Locale("ru", "RU"))
    return formatter.format(this / 100)
}