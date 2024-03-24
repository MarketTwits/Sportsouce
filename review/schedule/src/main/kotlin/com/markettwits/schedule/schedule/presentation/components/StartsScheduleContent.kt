package com.markettwits.schedule.schedule.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.schedule.schedule.presentation.components.list.month_calendar.MonthCalendarContent
import com.markettwits.schedule.schedule.presentation.components.list.pager.StartsPagerCalendar
import com.markettwits.schedule.schedule.presentation.components.list.pager.defaultStartCalendarPages
import com.markettwits.schedule.schedule.presentation.components.list.week_calendar.WeekCalendarContent
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun StartsScheduleSuccessContent(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (List<StartsListItem>) -> Unit,
) {
    StartsPagerCalendar(
        modifier = modifier,
        pages = defaultStartCalendarPages(),
        content = { index ->
            when (index) {
                0 -> WeekCalendarContent(starts = starts) { onClick(it) }
                1 -> MonthCalendarContent(starts = starts) { onClick(it) }
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