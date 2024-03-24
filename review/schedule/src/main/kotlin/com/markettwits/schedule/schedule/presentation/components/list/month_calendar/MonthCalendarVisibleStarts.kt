package com.markettwits.schedule.schedule.presentation.components.list.month_calendar

import com.kizitonwose.calendar.core.CalendarMonth
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate

fun getMonthCalendarVisibleStarts(
    starts: Map<LocalDate, List<StartsListItem>>,
    visibleMonth: CalendarMonth,
): List<StartsListItem> =
    starts.entries.mapNotNull { entry ->
        val condition =
            entry.key.monthValue == visibleMonth.yearMonth.monthValue && entry.key.year == visibleMonth.yearMonth.year
        val item = if (condition)
            entry.value.find {
                it.date.parseStringToLocalDateTime() == entry.key
            }
        else null
        item
    }