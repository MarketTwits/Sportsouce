package com.markettwits.sportsouce.club.dashboard.presentation.components.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.sportsouce.club.dashboard.presentation.components.title.MainDashboardTitle
import com.markettwits.sportsouce.club.info.domain.models.Schedule

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ScheduleContent(
    modifier: Modifier = Modifier,
    schedule: List<Schedule>,
    onClick: (Schedule) -> Unit
) {
    val categories = schedule.map { it.kindOfSport }.toSet()

    var selectedCategory by rememberSaveable { mutableStateOf("") }

    val currentSchedule = if (selectedCategory.isEmpty()) {
        schedule
    } else {
        schedule.filter { it.kindOfSport == selectedCategory }
    }

    Column(modifier = modifier) {
        MainDashboardTitle(
            modifier = Modifier.padding(10.dp),
            title = "Расписание тренировок",
            description = "Расписание актуально на текущую неделю и обновляется в воскресенье"
        )
        ScheduleCategoriesContent(
            modifier = Modifier.padding(10.dp),
            categories = categories.toList(),
            selectedCategory = selectedCategory,
            onClick = {
                selectedCategory = if (selectedCategory == it) "" else it
            }
        )
        AnimatedContent(
            targetState = currentSchedule,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { scheduleList ->
            FlowRow(
                maxItemsInEachRow = 10,
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center
            ) {
                scheduleList.forEach { item ->
                    ScheduleItem(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(10.dp),
                        schedule = item,
                        onClick = onClick
                    )
                }
            }
        }
    }
}