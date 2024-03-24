package com.markettwits.schedule.schedule.presentation.components.list.pager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewWeek

fun defaultStartCalendarPages(): List<PagerTopBar> =
    listOf(
        PagerTopBar(
            "Неделя",
            icon = Icons.Default.CalendarViewWeek
        ),
        PagerTopBar(
            "Месяц",
            icon = Icons.Default.CalendarMonth
        )
    )