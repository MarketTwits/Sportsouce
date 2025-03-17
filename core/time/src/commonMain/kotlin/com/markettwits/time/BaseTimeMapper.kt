package com.markettwits.time

import com.raedghazal.kotlinx_datetime_ext.LocalDateTimeFormatter
import com.raedghazal.kotlinx_datetime_ext.Locale
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Implementation of TimeMapper using kotlinx.datetime and
 * kotlinx_datetime_ext.Locale
 */
class BaseTimeMapper : TimeMapper {

    override fun mapTime(timePattern: TimePattern, time: String): String {

        val formatter = LocalDateTimeFormatter.ofPattern(
            pattern = timePattern.map(),
            locale = Locale.forLanguageTag(LANGUAGE_RU_TAG)
        )

        val localDateTime = Instant.parse(time).toLocalDateTime(TimeZone.UTC)

        return formatter.format(localDateTime)
    }

    override fun mapTime(timePattern: TimePattern, time: Long?): String {
        if (time == null) return ""

        val instant = Instant.fromEpochMilliseconds(time)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val formatter = LocalDateTimeFormatter.ofPattern(
            pattern = timePattern.map(),
            locale = Locale.forLanguageTag(LANGUAGE_RU_TAG)
        )

        return formatter.format(localDateTime)
    }

    override fun mapTimeToCloud(timePattern: TimePattern, time: String): String {

        val formatter = when (timePattern) {
            is TimePattern.Full -> LocalDateTimeFormatter.ofPattern(
                timePattern.map(),
                Locale.forLanguageTag(LANGUAGE_RU_TAG)
            )

            is TimePattern.FullWithEmptySpace -> LocalDateTimeFormatter.ofPattern(
                timePattern.map(),
                Locale.forLanguageTag(LANGUAGE_RU_TAG)
            )

            is TimePattern.FullWithDots -> LocalDateTimeFormatter.ofPattern(
                timePattern.map(),
                Locale.forLanguageTag(LANGUAGE_RU_TAG)
            )

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

        val instant = localDateTime.toInstant(TimeZone.UTC)

        return instant.toString()
    }

    companion object {
        const val LANGUAGE_RU_TAG = "ru-RU"
    }
}