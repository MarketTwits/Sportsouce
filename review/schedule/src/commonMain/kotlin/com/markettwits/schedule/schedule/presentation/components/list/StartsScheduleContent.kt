package com.markettwits.schedule.schedule.presentation.components.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.schedule.schedule.presentation.components.list.calendar_pager.StartsPagerCalendar
import com.markettwits.schedule.schedule.presentation.components.list.calendar_pager.defaultStartCalendarPages
import com.markettwits.schedule.schedule.presentation.components.list.month_calendar.MonthCalendarContent
import com.markettwits.schedule.schedule.presentation.components.list.week_calendar.WeekCalendarContent
import com.markettwits.schedule.schedule.presentation.components.list.year_calendar.YearCalendarContent
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.launch
import java.time.YearMonth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartsScheduleSuccessContent(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (List<StartsListItem>) -> Unit,
) {
    val pagerState =
        rememberPagerState(pageCount = defaultStartCalendarPages()::size, initialPage = 1)
    val scope = rememberCoroutineScope()
    var month by rememberSaveable {
        mutableStateOf(YearMonth.now())
    }
    StartsPagerCalendar(
        modifier = modifier,
        state = pagerState,
        pages = defaultStartCalendarPages(),
        content = { index ->
            when (index) {
                0 -> WeekCalendarContent(starts = starts) { onClick(it) }
                1 -> MonthCalendarContent(
                    starts = starts,
                    onClickStart = {
                        onClick(it)
                    },
                    selectMonth = month
                )

                2 -> YearCalendarContent(
                    starts = starts,
                    onClickStart = { onClick(it) },
                    onClickMonth = {
                        month = it
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun StartsScheduleLoadingContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        content = {
            LoadingFullScreen()
        })
}

@Composable
fun StartsScheduleFailedContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onClickPop: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        content = {
            FailedScreen(onClickRetry = onClick::invoke, onClickBack = { onClickPop() })
        })
}