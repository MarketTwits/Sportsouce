package com.markettwits.schedule.schedule.data

import com.markettwits.schedule.schedule.domain.StartsSchedule
import com.markettwits.starts_common.domain.StartsListItem
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

interface StartScheduleMapper {
    fun map(items: List<StartsListItem>): List<StartsSchedule>
}

class StartScheduleMapperBase : StartScheduleMapper {
    override fun map(items: List<StartsListItem>): List<StartsSchedule> {
        val currentDate = LocalDate.now()
        val startOfWeek =
            currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - DayOfWeek.MONDAY.value)

        return DayOfWeek.entries.map { dayOfWeek ->
            val currentDay =
                startOfWeek.plusDays((dayOfWeek.value - DayOfWeek.MONDAY.value).toLong())
            val day =
                currentDay.dayOfWeek.getDisplayName(TextStyle.FULL, Locale(RU_LOCAL_WEEK_FIELD))
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val itemsForDay = items
                .filter {
                    LocalDate.parse(it.date, DateTimeFormatter.ofPattern("d MMMM yyyy"))
                        .isEqual(currentDay)
                }
            StartsSchedule(day, "empty", itemsForDay)
        }
    }

    companion object {
        const val RU_LOCAL_WEEK_FIELD = "ru-RU"
    }

}