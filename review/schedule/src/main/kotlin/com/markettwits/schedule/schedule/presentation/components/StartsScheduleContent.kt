package com.markettwits.schedule.schedule.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import com.markettwits.core_ui.failed_screen.FailedScreen
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.schedule.schedule.domain.StartsSchedule
import com.markettwits.schedule.schedule.presentation.store.StartsScheduleStore

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
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                strokeCap = StrokeCap.Round,
                color = SportSouceColor.SportSouceBlue
            )
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