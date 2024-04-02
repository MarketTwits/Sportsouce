package com.markettwits.schedule.schedule.presentation.components.list.month_calendar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.CalendarDay
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.CompactCalendarDay
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate

@Composable
fun MonthCalendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    startTime: Map<LocalDate, List<StartsListItem>>,
    userScrollEnabled: Boolean = true,
    onClickStart: ((List<StartsListItem>) -> Unit)? = null,
    header: @Composable() ((Modifier, CalendarMonth) -> Unit)? = null,
) {
    HorizontalCalendar(
        modifier = modifier.wrapContentWidth(),
        userScrollEnabled = userScrollEnabled,
        state = state,
        dayContent = { day ->
            val colors = if (day.position == DayPosition.MonthDate) {
                startTime[day.date].orEmpty()
            } else {
                emptyList()
            }
            CalendarDay(
                day = day,
                starts = colors,
            ) { clicked ->
                if (startTime[clicked.date] != null)
                    if (onClickStart != null) {
                        startTime[clicked.date]?.let { onClickStart(it) }
                    }
            }
        },
        monthHeader = {
            header?.let { it1 -> it1(Modifier.padding(vertical = 8.dp), it) }
        },
    )
}

@Composable
fun CompactMonthCalendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    startTime: Map<LocalDate, List<StartsListItem>>,
    userScrollEnabled: Boolean = false,
    calendarScrollPaged: Boolean = false,
    header: @Composable() ((Modifier, CalendarMonth) -> Unit)? = null,
) {
    HorizontalCalendar(
        modifier = modifier
            .wrapContentWidth(),
        userScrollEnabled = userScrollEnabled,
        calendarScrollPaged = calendarScrollPaged,
        state = state,
        dayContent = { day ->
            val starts = if (day.position == DayPosition.MonthDate) {
                startTime[day.date].orEmpty()
            } else {
                emptyList()
            }
            CompactCalendarDay(
                day = day,
                starts = starts,
            )
        },
        monthHeader = {
            header?.let { it1 -> it1(Modifier.padding(vertical = 8.dp), it) }
        },
    )
}