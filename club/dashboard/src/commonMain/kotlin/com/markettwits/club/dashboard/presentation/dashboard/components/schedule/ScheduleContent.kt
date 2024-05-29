package com.markettwits.club.dashboard.presentation.dashboard.components.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.club.dashboard.presentation.dashboard.components.title.MainDashboardTitle
import com.markettwits.club.info.domain.models.Schedule

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ScheduleContent(
    modifier: Modifier = Modifier,
    schedule: List<Schedule>,
    onClick: (Schedule) -> Unit
) {
    Column(modifier = modifier) {
        MainDashboardTitle(
            modifier = Modifier.padding(10.dp),
            title = "Расписание тренировок",
            description = "Расписание актуально на текующую неделю и обновляется в воскресенье"
        )
        FlowRow(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            maxItemsInEachRow = 10,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            schedule.forEach {
                ScheduleItem(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(10.dp),
                    schedule = it,
                    onClick = onClick
                )
            }
        }
    }
}