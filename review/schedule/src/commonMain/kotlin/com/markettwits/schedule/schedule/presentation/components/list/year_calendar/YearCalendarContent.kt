package com.markettwits.schedule.schedule.presentation.components.list.year_calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.CalendarSimpleTitle
import com.markettwits.schedule.schedule.presentation.components.list.common.calendar.parseStringToLocalDateTime
import com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info.KindOfSportsListInfo
import com.markettwits.schedule.schedule.presentation.components.list.common.starts_list_info.StartsListInfo
import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth


@Composable
fun YearCalendarContent(
    starts: List<StartsListItem>,
    onClickStart: (List<StartsListItem>) -> Unit,
    onClickMonth: (YearMonth) -> Unit,
) {
    val currentYear by rememberSaveable { mutableStateOf(Year.now()) }
    val startTime = starts.groupBy { it.date.parseStringToLocalDateTime() }
    val yearsRange = rememberInfiniteListState(
        initialValue = currentYear.value,
        minItem = currentYear.value - 5,
        maxItem = currentYear.value + 1
    )
    val startsInYear: List<StartsListItem> =
        getYearCalendarVisibleStarts(startTime, Year.of(yearsRange.value))
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {
        CalendarSimpleTitle(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            eventsCount = startsInYear.size,
            title = Year.of(yearsRange.value).value.toString(),
            goToPrevious = {
                yearsRange.value -= 1
                // currentYear = currentYear.minusYears(1)
            },
            goToNext = {
                yearsRange.value += 1
                // currentYear = currentYear.plusYears(1)
            },
        )
        val startsByDate: Map<LocalDate, List<StartsListItem>> =
            starts.groupBy { it.date.parseStringToLocalDateTime() }


        YearCalendar(
            year = Year.of(yearsRange.value),
            starts = startsByDate,
            onClickMonth = { onClickMonth(it) }
        )
        StartsListInfo(startsListItem = startsInYear)
        KindOfSportsListInfo(startsListItem = startsInYear) {
            onClickStart(it)
        }
    }
}

@Composable
fun rememberInfiniteListState(
    initialValue: Int,
    minItem: Int,
    maxItem: Int
): MutableState<Int> {
    val currentValue = rememberSaveable { mutableIntStateOf(initialValue) }
    if (currentValue.intValue < minItem) {
        currentValue.intValue = minItem
    } else if (currentValue.intValue > maxItem) {
        currentValue.intValue = maxItem
    }
    return currentValue
}