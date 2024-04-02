package com.markettwits.schedule.schedule.presentation.components.list.common.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info.KindOfSportsInfo
import com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info.KindOfSportsInfoMapper
import com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info.KindOfSportsInfoMapperBase
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun CalendarDay(
    day: CalendarDay,
    starts: List<StartsListItem> = emptyList(),
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
                DayPosition.MonthDate -> MaterialTheme.colorScheme.tertiary
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
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (starts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                            .clip(Shapes.medium)
                            .background(circleShapeColor)
                    )
                } else {
                    val kindOfSport: List<KindOfSportsInfo> = KindOfSportsInfoMapperBase.mapStarts(
                        starts,
                        MaterialTheme.colorScheme.tertiary
                    )
                    kindOfSport.forEachIndexed { index, start ->
                        DayCircle(
                            baseSize = 14.dp,
                            start.color,
                            index = index,
                            kindOfSport.size
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun CompactCalendarDay(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    starts: List<StartsListItem> = emptyList(),
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(CircleShape)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val circleShapeColor = when (day.position) {
                DayPosition.MonthDate -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                DayPosition.InDate, DayPosition.OutDate -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (starts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                            .clip(Shapes.medium)
                            .background(circleShapeColor)
                    )
                } else {
                    val kindOfSport: List<KindOfSportsInfo> = KindOfSportsInfoMapperBase.mapStarts(
                        starts,
                        MaterialTheme.colorScheme.tertiary
                    )
                    kindOfSport.forEachIndexed { index, start ->
                        DayCircle(
                            baseSize = 10.dp,
                            color = start.color,
                            index = index,
                            listSize = kindOfSport.size
                        )
                        val boxSize =
                            ((10.dp - (index * 2).dp) * (kindOfSport.size - index)).coerceAtMost(34.dp) // Decrease size with increasing index
                        val margin = (150.dp - boxSize) / 2
                        Box(
                            modifier = Modifier
                                .size(boxSize)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                                .background(start.color)
                                .padding(margin)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.DayCircle(baseSize: Dp, color: Color, index: Int, listSize: Int) {
    val boxSize =
        ((baseSize - (index * 2).dp) * (listSize - index)).coerceAtMost(32.dp) // Decrease size with increasing index
    val margin = (150.dp - boxSize) / 2
    Box(
        modifier = Modifier
            .size(boxSize)
            .clip(CircleShape)
            .align(Alignment.Center)
            .background(color)
            .padding(margin)
    )
}