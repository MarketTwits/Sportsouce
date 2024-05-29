package com.markettwits.schedule.schedule.presentation.components.list.week_calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.CalendarDay
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.CalendarSimpleTitle
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.WeekHeader
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.getWeekPageTitle
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.rememberFirstVisibleWeekAfterScroll
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.toCalendarDate
import com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info.KindOfSportsListInfo
import com.markettwits.schedule.schedule.presentation.components.list.common.starts_list_info.StartsListInfo
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.launch
import java.time.LocalDate


@Composable
fun WeekCalendarContent(
    starts: List<StartsListItem>,
    onClickItem: (List<StartsListItem>) -> Unit,
) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val startTime =
        starts.groupBy { it.date.parseStringToLocalDateTime() } // Map<LocalDate, List<StartsListItem>>
    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state) // Week
    val startsInWeek: List<StartsListItem> = getWeekCalendarVisibleStarts(startTime, visibleWeek)
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        CalendarSimpleTitle(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            eventsCount = startsInWeek.size,
            title = getWeekPageTitle(visibleWeek),
            goToPrevious = {
                val targetDate = state.firstVisibleWeek.days.first().date.minusDays(1)
                coroutineScope.launch {
                    state.animateScrollToWeek(targetDate)
                }
            },
            goToNext = {
                val targetDate = state.firstVisibleWeek.days.last().date.plusDays(1)
                coroutineScope.launch {
                    state.animateScrollToWeek(targetDate)
                }
            },
        )
        WeekCalendar(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .sizeIn(minWidth = 800.dp, maxWidth = 800.dp),
            state = state,
            dayContent = { day ->
                val colors = if (day.position == WeekDayPosition.RangeDate) {
                    startTime[day.date].orEmpty()
                } else {
                    emptyList()
                }
                CalendarDay(
                    day = day.date.toCalendarDate(),
                    starts = colors
                ) { clicked ->
                    selection = clicked
                    val item = startTime[selection?.date]
                    if (item != null)
                        onClickItem(item)
                }
            },
            weekHeader = {
                WeekHeader(
                    modifier = Modifier.padding(vertical = 8.dp),
                    daysOfWeek = it.days,
                )
            }
        )
        StartsListInfo(startsListItem = startsInWeek)
        KindOfSportsListInfo(startsListItem = startsInWeek, onClick = {
            onClickItem(it)
        })
    }
}