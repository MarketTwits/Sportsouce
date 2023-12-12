package com.markettwits.starts.data

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


interface TimeMapper{
    fun mapTime(formatter: TimePattern, time : String) : String
}

class BaseTimeMapper : TimeMapper {
    override fun mapTime(timePattern: TimePattern, time: String): String {
        val instant = Instant.parse(time)
        val dateTime = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId())
        val formatter = DateTimeFormatter.ofPattern(timePattern.map())
        return dateTime.format(formatter)
    }
}
interface TimePattern{
    fun map() : String
    object ddMMMMyyyy : TimePattern {
        override fun map() = "dd MMMM yyyy"
    }
}
