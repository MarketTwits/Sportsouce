package com.markettwits.schedule.schedule.presentation.components.list.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.starts_common.presentation.StartCard
import kotlinx.coroutines.launch
import java.time.YearMonth


@Composable
fun MonthCalendarContent(starts: List<StartsListItem>, onClickStart: (Int) -> Unit) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    var startsInMonth: Int by remember { mutableIntStateOf(0) }
    val daysOfWeek = remember { daysOfWeek() }
    val startTime = starts.groupBy { it.date.parseStringToLocalDateTime() }
    val flightsInSelectedDate = remember {
        derivedStateOf {
            val date = selection?.date
            if (date == null) emptyList() else startTime[date].orEmpty()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
        LaunchedEffect(visibleMonth) {
            selection = null
        }
        startsInMonth = startTime.entries.count { entry ->
            entry.key.monthValue == visibleMonth.yearMonth.monthValue && entry.key.year == visibleMonth.yearMonth.year
        }
        SimpleCalendarTitle(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            eventsCount = startsInMonth,
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
        )
        HorizontalCalendar(
            modifier = Modifier.wrapContentWidth(),
            state = state,
            dayContent = { day ->
                val colors = if (day.position == DayPosition.MonthDate) {
                    startTime[day.date].orEmpty().map { Color.Black }
                } else {
                    emptyList()
                }
                Day(
                    day = day,
                    isSelected = selection == day,
                    colors = colors,
                ) { clicked ->
                    selection = clicked
                }

            },
            monthHeader = {
                MonthHeader(
                    modifier = Modifier.padding(vertical = 8.dp),
                    daysOfWeek = daysOfWeek,
                )
            },
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(items = flightsInSelectedDate.value) { flight ->
                StartCard(start = flight) {
                    onClickStart(it)
                }
            }
        }
    }
}