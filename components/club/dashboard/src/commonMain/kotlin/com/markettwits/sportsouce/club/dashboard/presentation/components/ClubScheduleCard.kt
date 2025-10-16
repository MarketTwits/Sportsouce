package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import com.markettwits.sportsouce.club.dashboard.presentation.screen.MenuItemColor

@Composable
internal fun ClubScheduleCard(onClick: () -> Unit) {
    ClubInfoCard(
        title = "Расписание тренировок",
        description = "Расписание актуально на текущую неделю и обновляется в воскресенье",
        icon = Icons.Default.CalendarMonth,
        colors = MenuItemColor.SCHEDULE.colors,
        onClick = onClick
    )
}