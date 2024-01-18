package com.markettwits.core_ui.base_extensions.date

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.mapToString(): String {
    val dateTime = LocalDate.parse(this.toString())
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return dateTime.format(formatter)
}