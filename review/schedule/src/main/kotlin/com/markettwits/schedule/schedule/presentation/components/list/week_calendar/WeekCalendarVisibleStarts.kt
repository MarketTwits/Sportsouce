package com.markettwits.schedule.schedule.presentation.components.list.week_calendar

import com.kizitonwose.calendar.core.Week
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate

fun getWeekCalendarVisibleStarts(
    starts: Map<LocalDate, List<StartsListItem>>,
    visibleWeek: Week,
): List<StartsListItem> {
    val visibleDays = visibleWeek.days.map { it.date }
    return starts.flatMap { (date, items) ->
        if (date in visibleDays) {
            items.filter { it.date.parseStringToLocalDateTime() == date }
        } else {
            emptyList()
        }
    }
}