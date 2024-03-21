package com.markettwits.schedule.schedule.presentation.components.list.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textColor = when (day.position) {
                DayPosition.MonthDate -> MaterialTheme.colorScheme.onPrimary
                DayPosition.InDate, DayPosition.OutDate -> Color.Transparent
            }
            val circleShapeColor = when (day.position) {
                DayPosition.MonthDate -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                DayPosition.InDate, DayPosition.OutDate -> Color.Transparent
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = day.date.dayOfMonth.toString(),
                fontFamily = FontNunito.bold,
                color = textColor,
                fontSize = 12.sp,
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (colors.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                            .clip(Shapes.medium)
                            .background(circleShapeColor)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .background(MaterialTheme.colorScheme.tertiary)

                    )
                }
            }
        }
    }
}