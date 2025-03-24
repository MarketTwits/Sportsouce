package com.markettwits.schedule.schedule.presentation.components.list.month_calendar

import com.kizitonwose.calendar.core.CalendarMonth
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate

internal fun getMonthCalendarVisibleStarts(
    starts: Map<LocalDate, List<StartsListItem>>,
    visibleMonth: CalendarMonth,
): List<StartsListItem> =
    starts.flatMap { (date, items) ->
        if (date.year == visibleMonth.yearMonth.year && date.monthValue == visibleMonth.yearMonth.monthValue) {
            items.filter { it.date.parseStringToLocalDateTime() == date }
        } else {
            emptyList()
        }
    }