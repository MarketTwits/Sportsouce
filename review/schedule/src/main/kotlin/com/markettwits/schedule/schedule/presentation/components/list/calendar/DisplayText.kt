package com.markettwits.schedule.schedule.presentation.components.list.calendar

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.util.Locale

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.getDefault())
}

fun String.parseStringToLocalDateTime(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("d MMMM uuuu")
    return try {
        LocalDate.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        println("Error parsing date: ${e.message}")
        throw IllegalArgumentException("unknown date : $this")
    }
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.getDefault()).let { value ->
        if (uppercase) value.uppercase(Locale.getDefault()) else value
    }
}