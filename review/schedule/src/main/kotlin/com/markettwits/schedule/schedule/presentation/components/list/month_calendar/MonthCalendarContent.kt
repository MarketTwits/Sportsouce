package com.markettwits.schedule.schedule.presentation.components.list.month_calendar

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.CalendarSimpleTitle
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.MonthHeader
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.displayText
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.rememberFirstCompletelyVisibleMonth
import com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info.KindOfSportsListInfo
import com.markettwits.schedule.schedule.presentation.components.list.common.starts_list_info.StartsListInfo
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.launch
import java.time.YearMonth


@Composable
fun MonthCalendarContent(
    starts: List<StartsListItem>,
    onClickStart: (List<StartsListItem>) -> Unit,
    selectMonth: YearMonth,
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val daysOfWeek = remember { daysOfWeek() }
    val startTime = starts.groupBy { it.date.parseStringToLocalDateTime() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfGrid,
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
    val startsInMonth: List<StartsListItem> = getMonthCalendarVisibleStarts(
        startTime,
        visibleMonth
    )
    LaunchedEffect(selectMonth) {
        state.animateScrollToMonth(selectMonth)
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {
        CalendarSimpleTitle(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            eventsCount = startsInMonth.size,
            title = visibleMonth.yearMonth.displayText(),
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
        MonthCalendar(
            state = state,
            startTime = startTime,
            header = { modifier, month ->
                MonthHeader(
                    modifier = Modifier.padding(vertical = 8.dp),
                    daysOfWeek = daysOfWeek,
                )
            },
            onClickStart = {
                onClickStart(it)
            }
        )
        StartsListInfo(startsListItem = startsInMonth)
        Log.e("mt05", "--------------------")
        Log.e("mt05", startsInMonth.size.toString())
        Log.e("mt05", startsInMonth.toString())
        KindOfSportsListInfo(startsListItem = startsInMonth) {
            onClickStart(it)
        }
    }
}