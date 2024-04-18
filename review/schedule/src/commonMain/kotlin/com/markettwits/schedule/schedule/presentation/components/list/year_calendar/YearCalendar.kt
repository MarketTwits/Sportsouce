package com.markettwits.schedule.schedule.presentation.components.list.year_calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.displayText
import com.markettwits.schedule.schedule.presentation.components.list.month_calendar.CompactMonthCalendar
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.util.Locale

@Composable
fun YearCalendar(
    year: Year,
    starts: Map<LocalDate, List<StartsListItem>>,
    onClickMonth: (YearMonth) -> Unit
) {
    val months = (1..12).chunked(3) // Splitting 12 months into chunks of 3
    Column {
        months.forEach { rowMonths ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                rowMonths.forEach { monthIndex ->
                    val currentYear = remember { mutableIntStateOf(year.value) }
                    val month = remember {
                        val yearMonth = YearMonth.of(currentYear.intValue, monthIndex)
                        if (yearMonth.year != currentYear.intValue) {
                            YearMonth.of(currentYear.intValue, monthIndex)
                        } else {
                            yearMonth
                        }
                    }
                    val startMonth = remember { month.minusMonths(100) }
                    val endMonth = remember { month.plusMonths(100) }
                    val daysOfWeek = remember { daysOfWeek() }

                    val state = rememberCalendarState(
                        startMonth = startMonth,
                        endMonth = endMonth,
                        firstVisibleMonth = month,
                        firstDayOfWeek = daysOfWeek.first(),
                        outDateStyle = OutDateStyle.EndOfGrid,
                    )
                    LaunchedEffect(key1 = year) {
                        state.scrollToMonth(YearMonth.of(year.value, month.month))
                    }

                    Column(
                        Modifier
                            .padding(5.dp)
                            .weight(1f)
                    ) {
                        CompactMonthCalendar(
                            modifier = Modifier
                                .clip(Shapes.medium)
                                .clickable {
                                    onClickMonth(state.firstVisibleMonth.yearMonth)
                                },
                            state = state,
                            startTime = starts,
                            header = { modifier, month ->
                                Text(
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    text = month.yearMonth.month.displayText(false)
                                        .replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        },
                                    fontFamily = FontNunito.medium()
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}