package com.markettwits.time

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


class ExtendedTimeMapper : TimeMapper, AbstractDateTimeFormatter() {

    override fun mapTime(formatter: TimePattern, time: String): String {
        // Parse the time string into Instant and convert to LocalDateTime based on system timezone
        val instant = Instant.parse(time)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Format the date to a string using the provided pattern
        return formatDateTime(dateTime, formatter.map())
    }

    override fun mapTimeToCloud(formatter: TimePattern, time: String): String {
        val localDateTime = LocalDateTime.parse(time) // assume ISO-8601 format

        // Convert LocalDateTime to Instant for UTC timezone
        val instant = localDateTime.toInstant(TimeZone.UTC)

        // Return formatted date-time string for cloud
        return formatDateTime(instant.toLocalDateTime(TimeZone.UTC), TimePattern.Remote.map())
    }
}
