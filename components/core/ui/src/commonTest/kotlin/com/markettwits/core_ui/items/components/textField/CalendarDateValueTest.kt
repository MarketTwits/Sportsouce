package com.markettwits.core_ui.items.components.textField

import androidx.compose.ui.text.AnnotatedString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CalendarDateValueTest {
    @Test
    fun datesRoundTripInDisplayAndIsoFormatsIncludingLeapDays() {
        for (text in listOf("29.02.2024", "01.01.1925", "31.12.2026")) {
            val millis = assertNotNull(calendarDateMillis(text, 1925..2026))
            assertEquals(text, calendarDateText(millis))
        }
        assertEquals(calendarDateMillis("29.02.2024", 1925..2026), calendarDateMillis("2024-02-29", 1925..2026))
    }

    @Test
    fun invalidDatesAndDatesOutsideTheExistingYearRangeAreRejected() {
        for (text in listOf("", "31.02.2024", "29.02.2023", "01.13.2024", "01.01.1924", "01.01.2027", "Дата")) {
            assertNull(calendarDateMillis(text, 1925..2026), text)
        }
    }

    @Test
    fun dateInputOffsetsRemainValidForEveryPartialValueAndSeparator() {
        val raw = "29022024"
        assertEquals("29.02.2024", CalendarInputTransformation.filter(AnnotatedString(raw)).text.text)
        for (length in 0..8) {
            val result = CalendarInputTransformation.filter(AnnotatedString(raw.take(length)))
            val mapping = result.offsetMapping
            val positions = (0..length).map { mapping.originalToTransformed(it) }
            assertEquals(positions.sorted(), positions)
            for (offset in 0..length) {
                val transformed = mapping.originalToTransformed(offset)
                assertTrue(transformed in 0..result.text.length)
                assertEquals(offset, mapping.transformedToOriginal(transformed))
            }
            for (offset in 0..result.text.length) assertTrue(mapping.transformedToOriginal(offset) in 0..length)
        }
    }
}
