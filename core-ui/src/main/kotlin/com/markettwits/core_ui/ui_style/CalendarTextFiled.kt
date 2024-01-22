package com.markettwits.core_ui.ui_style

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_extensions.date.mapToString
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTextFiled(
    modifier: Modifier = Modifier,
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (String) -> Unit
) {
    val calendarState = rememberUseCaseState()
    val range = rememberCalendarCloseRange()
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            locale = Locale.getDefault(),
            boundary = range,
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date { newDate ->
            onValueChanged(newDate.mapToString())
        },
    )
    textFiled(modifier.clickable {
        calendarState.show()
    })
}

interface CalendarBoundary {
    fun range(): ClosedRange<LocalDate>
    class All : CalendarBoundary {
        override fun range(): ClosedRange<LocalDate> {
            val DEFAULT_RANGE_START_DATE = LocalDate.of(1920, 3, 15)
            val DEFAULT_RANGE_END_YEAR_OFFSET = 20L
            val DEFAULT_RANGE_END_DATE = LocalDate.now().plusYears(DEFAULT_RANGE_END_YEAR_OFFSET)
                .withMonth(1)
                .withDayOfMonth(15)
            return DEFAULT_RANGE_START_DATE..DEFAULT_RANGE_END_DATE
        }
    }
}

@Composable
fun rememberCalendarCloseRange(): ClosedRange<LocalDate> =
    remember {
        mutableStateOf(CalendarBoundary.All().range())
    }.value