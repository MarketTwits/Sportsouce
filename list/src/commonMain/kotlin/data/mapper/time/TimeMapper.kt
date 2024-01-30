package data.mapper.time

interface TimeMapper {
    fun mapTime(formatter: TimePattern, time: String): String
    fun mapTimeToCloud(formatter: TimePattern = TimePattern.FullWithDots, time: String): String
}