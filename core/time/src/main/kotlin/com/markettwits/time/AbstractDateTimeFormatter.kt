package com.markettwits.time

import com.vanniktech.locale.Locale
import com.vanniktech.locale.Locales
import com.vanniktech.locale.toJavaLocale
import kotlinx.datetime.LocalDateTime

abstract class AbstractDateTimeFormatter {

    /**
     * Formats the given `LocalDateTime` object into a string based on the provided pattern.
     *
     * This method supports basic formatting by replacing the following placeholders in the pattern:
     * - `dd` — the day of the month, two digits with leading zero if necessary (01-31).
     * - `MM` — the month number, two digits with leading zero if necessary (01-12).
     * - `yyyy` — the full year, four digits (e.g., 2024).
     * - `HH` — hours in 24-hour format, two digits (00-23).
     * - `mm` — minutes, two digits with leading zero (00-59).
     * - `ss` — seconds, two digits with leading zero (00-59).
     *
     * For example, for the date `2024-05-20T13:45:30` and the pattern `"dd.MM.yyyy HH:mm"`,
     * the result will be `"20.05.2024 13:45"`.
     *
     * @param dateTime The `LocalDateTime` object to be formatted.
     * @param pattern The format string that defines how the date should be formatted.
     * @return A string representation of `dateTime`, formatted according to the specified `pattern`.
     */
    protected fun formatDateTime(dateTime: LocalDateTime, pattern: String): String {
        val locale = Locale.from(Locales.currentLocaleString())

        // Replace placeholders with localized values
        return pattern
            .replace("dd", dateTime.date.dayOfMonth.toString().padStart(2, '0'))
            .replace("MMMM", dateTime.date.month.name.replaceFirstChar { it.uppercase(locale.toJavaLocale()) }) // Full month name, localized
            .replace("yyyy", dateTime.date.year.toString())
            .replace("HH", dateTime.hour.toString().padStart(2, '0'))
            .replace("mm", dateTime.minute.toString().padStart(2, '0'))
            .replace("ss", dateTime.second.toString().padStart(2, '0'))
            .replace("EEEE", dateTime.dayOfWeek.name.replaceFirstChar { it.uppercase(locale.toJavaLocale()) }) // Full day name, localized
    }
}