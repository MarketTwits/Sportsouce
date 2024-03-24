package com.markettwits.schedule.schedule.presentation.components.list.week_calendar

import com.kizitonwose.calendar.core.Week
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate

fun getWeekCalendarVisibleStarts(
    starts: Map<LocalDate, List<StartsListItem>>,
    visibleWeek: Week,
): List<StartsListItem> =
    starts.entries.mapNotNull { entry ->
        val condition =
            entry.key in visibleWeek.days.map { it.date }
        val item = if (condition)
            entry.value.find {
                it.date.parseStringToLocalDateTime() == entry.key
            }
        else null
        item
    }