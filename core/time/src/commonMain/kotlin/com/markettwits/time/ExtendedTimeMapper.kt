package com.markettwits.time

import com.raedghazal.kotlinx_datetime_ext.LocalDateTimeFormatter
import com.raedghazal.kotlinx_datetime_ext.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Implementation of TimeMapper using kotlinx.datetime and kotlinx_datetime_ext.Locale
 */
class ExtendedTimeMapper : TimeMapper {

    override fun mapTime(timePattern: TimePattern, time: String): String {

        val formatter = LocalDateTimeFormatter.ofPattern(
            pattern = timePattern.map(),
            locale = Locale.default()
        )

        val localDateTime = Instant.parse(time).toLocalDateTime(TimeZone.currentSystemDefault())

        return formatter.format(localDateTime)
    }

    override fun mapTimeToCloud(timePattern: TimePattern, time: String): String {

        val formatter = when (timePattern) {
            is TimePattern.Full -> LocalDateTimeFormatter.ofPattern(timePattern.map(), Locale.default())
            is TimePattern.FullWithEmptySpace -> LocalDateTimeFormatter.ofPattern(timePattern.map(), Locale.default())
            is TimePattern.FullWithDots -> LocalDateTimeFormatter.ofPattern(timePattern.map(), Locale.default())
            else -> throw IllegalArgumentException("Unsupported pattern for input")
        }

        val localDateTime = when (timePattern) {
            is TimePattern.Full -> formatter.parseToLocalDateTime(time)
            is TimePattern.FullWithEmptySpace,
            is TimePattern.FullWithDots -> {
                val localDate = formatter.parseToLocalDate(time)
                localDate.atTime(0, 0)
            }
            else -> throw IllegalArgumentException("Unsupported pattern for input")
        }

        val instant = localDateTime.toInstant(TimeZone.currentSystemDefault())

        return instant.toString()
    }
}