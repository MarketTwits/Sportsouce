package com.markettwits.schedule.schedule.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.schedule.schedule.domain.StartsSchedule

@Composable
fun StartsScheduleSuccessContent(
    modifier: Modifier = Modifier,
    starts: List<StartsSchedule>,
    onClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        content = {
            items(starts) {
                ScheduleStartsRow(starts = it.items, day = it.day, onClick = onClick::invoke)
            }
        })
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
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        content = {
            FailedScreen(onClickHelp = { /*TODO*/ }, onClickRetry = onClick::invoke)
        })
}