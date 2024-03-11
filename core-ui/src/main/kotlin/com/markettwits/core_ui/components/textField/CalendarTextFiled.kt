package com.markettwits.core_ui.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_extensions.date.mapDateToString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTextFiled(
    modifier: Modifier = Modifier,
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (String) -> Unit
) {
//    val calendarState = rememberUseCaseState()
//    val range = rememberCalendarCloseRange()
//    CalendarDialog(
//        state = calendarState,
//        config = CalendarConfig(
//            locale = Locale.getDefault(),
//            boundary = range,
//            yearSelection = true,
//            monthSelection = true,
//            style = CalendarStyle.MONTH,
//        ),
//        selection = CalendarSelection.Date { newDate ->
//            onValueChanged(newDate.mapToString())
//        },
//    )
//    textFiled(modifier.clickable {
//        calendarState.show()
//    })

    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                    datePickerState.selectedDateMillis?.let { onValueChanged(it.mapDateToString()) }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    textFiled(modifier.clickable {
        openDialog = true
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