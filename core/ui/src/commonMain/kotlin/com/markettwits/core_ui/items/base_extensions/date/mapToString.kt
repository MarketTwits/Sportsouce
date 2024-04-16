package com.markettwits.core_ui.items.base_extensions.date

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

fun LocalDate.mapDateToString(): String {
    val dateTime = LocalDate.parse(this.toString())
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return dateTime.format(formatter)
}


fun Long.mapDateToString(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val date = Date(this)
    return sdf.format(date)
}