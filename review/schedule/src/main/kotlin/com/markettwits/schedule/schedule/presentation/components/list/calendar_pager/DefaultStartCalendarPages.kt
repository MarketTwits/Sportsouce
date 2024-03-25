package com.markettwits.schedule.schedule.presentation.components.list.calendar_pager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.CalendarViewWeek

fun defaultStartCalendarPages(): List<PagerTopBar> =
    listOf(
        PagerTopBar(
            title = "Неделя",
            icon = Icons.Default.CalendarViewWeek
        ),
        PagerTopBar(
            title = "Месяц",
            icon = Icons.Default.CalendarMonth
        ),
        PagerTopBar(
            "Год",
            icon = Icons.Default.CalendarViewMonth
        )
    )