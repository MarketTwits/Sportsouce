package data.mapper.time

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

class TimeMapperBase : TimeMapper {
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