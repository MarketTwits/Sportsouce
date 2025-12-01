package com.markettwits.core.time

import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

/**
 * Implementation of TimeMapper using kotlinx.datetime
 */
class BaseTimeMapper : TimeMapper {

    @OptIn(ExperimentalTime::class)
    override fun mapTime(timePattern: TimePattern, time: String): String {
        val instant = kotlin.time.Instant.parse(time)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        return when (timePattern) {
            is TimePattern.Full -> {
                "${localDateTime.day.toString().padStart(2, '0')}." +
                        "${localDateTime.month.number.toString().padStart(2, '0')}." +
                        "${localDateTime.year} " +
                        "${localDateTime.hour.toString().padStart(2, '0')}:" +
                        localDateTime.minute.toString().padStart(2, '0')
            }

            is TimePattern.FullWithEmptySpace -> {
                "${localDateTime.day} " +
                        "${getMonthName(localDateTime.month.number)} " +
                        "${localDateTime.year}"
            }

            is TimePattern.FullWithDots -> {
                "${localDateTime.day.toString().padStart(2, '0')}." +
                        "${localDateTime.month.number.toString().padStart(2, '0')}." +
                        "${localDateTime.year}"
            }

            is TimePattern.Remote -> {
                time
            }

            else -> throw IllegalArgumentException("Неподдерживаемый шаблон времени: $timePattern")
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun mapTime(timePattern: TimePattern, time: Long?): String {
        if (time == null) return ""

        val instant = kotlin.time.Instant.fromEpochMilliseconds(time)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        return when (timePattern) {
            is TimePattern.Full -> {
                "${localDateTime.day.toString().padStart(2, '0')}." +
                        "${localDateTime.month.number.toString().padStart(2, '0')}." +
                        "${localDateTime.year} " +
                        "${localDateTime.hour.toString().padStart(2, '0')}:" +
                        "${localDateTime.minute.toString().padStart(2, '0')}"
            }

            is TimePattern.FullWithEmptySpace -> {
                "${localDateTime.day} " +
                        "${getMonthName(localDateTime.month.number)} " +
                        "${localDateTime.year}"
            }

            is TimePattern.FullWithDots -> {
                "${localDateTime.day.toString().padStart(2, '0')}." +
                        "${localDateTime.month.number.toString().padStart(2, '0')}." +
                        "${localDateTime.year}"
            }

            is TimePattern.Remote -> {
                instant.toString()
            }

            else -> throw IllegalArgumentException("Неподдерживаемый шаблон времени: $timePattern")
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun mapTimeToCloud(timePattern: TimePattern, time: String): String {
        val localDateTime = when (timePattern) {
            is TimePattern.Full -> {
                val parts = time.split(" ")
                val datePart = parts[0]
                val timePart = parts.getOrNull(1) ?: "00:00"

                val dateNumbers = datePart.split(".")
                val day = dateNumbers[0].toInt()
                val month = dateNumbers[1].toInt()
                val year = dateNumbers[2].toInt()

                val timeNumbers = timePart.split(":")
                val hour = timeNumbers[0].toInt()
                val minute = timeNumbers[1].toInt()

                LocalDateTime(year, month, day, hour, minute)
            }

            is TimePattern.FullWithEmptySpace -> {
                val parts = time.split(" ")
                val day = parts[0].toInt()
                val monthName = parts[1]
                val year = parts[2].toInt()
                val month = getMonthNumber(monthName)

                LocalDateTime(year, month, day, 0, 0)
            }

            is TimePattern.FullWithDots -> {
                val parts = time.split(".")
                val day = parts[0].toInt()
                val month = parts[1].toInt()
                val year = parts[2].toInt()

                LocalDateTime(year, month, day, 0, 0)
            }

            else -> throw IllegalArgumentException("Неподдерживаемый шаблон для ввода")
        }

        return localDateTime.toInstant(TimeZone.UTC).toString()
    }

    private fun getMonthName(monthNumber: Int): String {
        val months = listOf(
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"
        )
        return months[monthNumber - 1]
    }

    private fun getMonthNumber(monthName: String): Int {
        val months = mapOf(
            "января" to 1, "февраля" to 2, "марта" to 3, "апреля" to 4,
            "мая" to 5, "июня" to 6, "июля" to 7, "августа" to 8,
            "сентября" to 9, "октября" to 10, "ноября" to 11, "декабря" to 12
        )
        return months[monthName] ?: throw IllegalArgumentException("Неизвестный месяц: $monthName")
    }

    companion object {
        const val LANGUAGE_RU_TAG = "ru-RU"
    }
}