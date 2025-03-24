package com.markettwits.schedule.schedule.presentation.components.list.common.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.WeekDay
import com.markettwits.core_ui.items.theme.FontNunito
import java.time.DayOfWeek


@Composable
fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            WeekHeaderItem(
                modifier = Modifier.weight(1f),
                dayOfWeek = dayOfWeek
            )
        }
    }
}

@Composable
fun WeekHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<WeekDay> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            WeekHeaderItem(
                modifier = Modifier.weight(1f),
                dayOfWeek = dayOfWeek.date.dayOfWeek
            )
        }
    }
}

@Composable
fun WeekHeaderItem(modifier: Modifier = Modifier, dayOfWeek: DayOfWeek) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        text = dayOfWeek.displayText(uppercase = true),
        fontFamily = FontNunito.medium()
    )
}