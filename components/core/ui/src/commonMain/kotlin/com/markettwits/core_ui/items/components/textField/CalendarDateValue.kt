package com.markettwits.core_ui.items.components.textField

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal fun calendarDateMillis(value: String, yearRange: IntRange): Long? {
    val date = runCatching {
        val text = value.trim()
        if (Regex("\\d{2}\\.\\d{2}\\.\\d{4}").matches(text)) {
            LocalDate(text.substring(6).toInt(), text.substring(3, 5).toInt(), text.substring(0, 2).toInt())
        } else LocalDate.parse(text)
    }.getOrNull() ?: return null
    return date.takeIf { it.year in yearRange }?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds()
}

@OptIn(ExperimentalTime::class)
internal fun calendarDate(millis: Long): LocalDate = Instant.fromEpochMilliseconds(millis)
    .toLocalDateTime(TimeZone.UTC).date

internal fun calendarDateText(millis: Long): String = calendarDate(millis).let { date ->
    "${date.day.toString().padStart(2, '0')}.${date.month.number.toString().padStart(2, '0')}.${date.year.toString().padStart(4, '0')}"
}

internal fun calendarDateDescription(date: LocalDate): String {
    val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
    return "${date.day} ${months[date.month.number - 1]} ${date.year}"
}

internal fun calendarInputText(raw: String): String = buildString {
    raw.take(8).forEachIndexed { index, char ->
        if (index == 2 || index == 4) append('.')
        append(char)
    }
}

internal object CalendarInputTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = calendarInputText(text.text)
        return TransformedText(AnnotatedString(formatted), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val position = offset.coerceIn(0, text.length.coerceAtMost(8))
                return position + (if (position > 2) 1 else 0) + (if (position > 4) 1 else 0)
            }
            override fun transformedToOriginal(offset: Int): Int = formatted.take(offset.coerceIn(0, formatted.length))
                .count(Char::isDigit).coerceAtMost(text.length)
        })
    }
}
