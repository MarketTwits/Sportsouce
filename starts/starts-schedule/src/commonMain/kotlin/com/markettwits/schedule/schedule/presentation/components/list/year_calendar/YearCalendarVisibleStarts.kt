package com.markettwits.schedule.schedule.presentation.components.list.year_calendar

import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate
import java.time.Year

fun getYearCalendarVisibleStarts(
    starts: Map<LocalDate, List<StartsListItem>>,
    year: Year
): List<StartsListItem> =
    starts.entries.flatMap { (startDate, items) ->
        if (startDate.year == year.value) {
            items
        } else {
            emptyList()
        }
    }