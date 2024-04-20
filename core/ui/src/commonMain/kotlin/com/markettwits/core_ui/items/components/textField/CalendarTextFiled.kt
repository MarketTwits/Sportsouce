package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.base_extensions.mapDateToString
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTextFiled(
    modifier: Modifier = Modifier,
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (String) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        val datePickerState = rememberDatePickerState(
            yearRange = IntRange(1900, LocalDate.now().year)
        )
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
                    enabled = confirmEnabled
                ) {
                    Text("ОК")
                    datePickerState.selectedDateMillis?.let { onValueChanged(it.mapDateToString()) }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Отменить")
                }
            },
            colors = sportSouceCalendarColors()
        ) {
            DatePicker(
                state = datePickerState,
                colors = sportSouceCalendarColors()
            )
        }
    }
    textFiled(modifier.clickable {
        openDialog = true
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun sportSouceCalendarColors(): DatePickerColors = DatePickerDefaults.colors(
    containerColor = MaterialTheme.colorScheme.primary,
    titleContentColor = MaterialTheme.colorScheme.onTertiary,
    headlineContentColor = MaterialTheme.colorScheme.tertiary,
    weekdayContentColor = MaterialTheme.colorScheme.tertiary,
    subheadContentColor = MaterialTheme.colorScheme.tertiary,
    navigationContentColor = MaterialTheme.colorScheme.tertiary,
    yearContentColor = MaterialTheme.colorScheme.tertiary,
    disabledYearContentColor = MaterialTheme.colorScheme.onBackground,
    currentYearContentColor = MaterialTheme.colorScheme.tertiary,

    selectedYearContentColor = MaterialTheme.colorScheme.onSecondary,
    disabledSelectedYearContentColor = MaterialTheme.colorScheme.onBackground,
    selectedYearContainerColor = MaterialTheme.colorScheme.secondary,
    disabledSelectedYearContainerColor = MaterialTheme.colorScheme.primaryContainer,
    dayContentColor = MaterialTheme.colorScheme.tertiary,
    disabledDayContentColor = MaterialTheme.colorScheme.onBackground,
    selectedDayContentColor = MaterialTheme.colorScheme.onSecondary,
    disabledSelectedDayContentColor = MaterialTheme.colorScheme.onBackground,
    selectedDayContainerColor = MaterialTheme.colorScheme.secondary,
    disabledSelectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
    todayContentColor = MaterialTheme.colorScheme.tertiary,
    todayDateBorderColor = MaterialTheme.colorScheme.outline,
    dayInSelectionRangeContentColor = MaterialTheme.colorScheme.onBackground,
    dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primaryContainer,
    dividerColor = Color.Transparent,
    dateTextFieldColors = defaultOutlineTextFiledColors()
)
