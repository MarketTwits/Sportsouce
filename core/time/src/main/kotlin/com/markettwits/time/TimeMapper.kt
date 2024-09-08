package com.markettwits.time

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

interface TimeMapper {

    fun mapTime(formatter: TimePattern, time: String): String

    fun mapTimeToCloud(formatter: TimePattern = TimePattern.FullWithDots, time: String): String
}

class BaseTimeMapper : TimeMapper {

    override fun mapTime(timePattern: TimePattern, time: String): String {
        val instant = Instant.parse(time)
        val dateTime = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId())
        val formatter = DateTimeFormatter.ofPattern(timePattern.map())
        return dateTime.format(formatter)
    }

    override fun mapTimeToCloud(formatter: TimePattern, time: String): String {
        val inputFormat = SimpleDateFormat(formatter.map(), Locale.getDefault())
        val pareseDate = inputFormat.parse(time)
        val zoneId = ZoneId.of("Z")
        val zonedDateTime = ZonedDateTime.ofInstant(pareseDate.toInstant(), zoneId)
        val outputFormat = DateTimeFormatter.ofPattern(TimePattern.Remote.map())
        return outputFormat.format(zonedDateTime)
    }
}

interface TimePattern {

    fun map(): String

    /** 20.05.2024 00:00 */
    object Full : TimePattern {
        override fun map() = "dd.MM.yyyy HH:mm"
    }

    /** 20 05 2024 */
    object FullWithEmptySpace : TimePattern {
        override fun map() = "dd MMMM yyyy"
    }

    /** 20.05.2024 */
    object FullWithDots : TimePattern {
        override fun map() = "dd.MM.yyyy"
    }

    /** 2024-07-31T17:00:00.000Z */
    object Remote : TimePattern {
        override fun map() = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    }
}