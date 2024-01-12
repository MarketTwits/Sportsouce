package com.markettwits.start_filter.start_filter.presentation.components

import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun LocalDate.mapToString(): String {
    val dateTime = LocalDate.parse(this.toString())
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return dateTime.format(formatter)
}
fun LocalDate.mapToStroke() : String{
    val dateTime = LocalDate.parse(this.toString())
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime.format(formatter)
}
