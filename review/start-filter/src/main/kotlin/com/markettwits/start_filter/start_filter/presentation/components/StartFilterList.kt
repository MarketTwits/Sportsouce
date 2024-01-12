package com.markettwits.start_filter.start_filter.presentation.components

import android.text.TextUtils.split
import android.util.Range
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.start_filter.start_filter.presentation.components.dialog.BottomScreenContent
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import kotlinx.datetime.LocalDate
import java.awt.font.NumericShaper
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartFilterList(
    modifier: Modifier = Modifier,
    startFilter: StartFilterUi,
    onEvent: (String, Int, Boolean) -> Unit
) {
    LazyColumn {
        itemsIndexed(startFilter.items) { index, item ->
            when (item.type) {
                is StartFilterUi.FilterStartType.Dialog -> {
                    val openAlertDialog = remember { mutableStateOf(false) }
                    MenuTextField(
                        enabled = false,
                        modifier = modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                            .clickable {
                                openAlertDialog.value = true
                            },
                        label = item.label,
                        value = item.selected.joinToString(", "),
                        onValueChange = {}
                    )
                    if (openAlertDialog.value){
                        BottomScreenContent(
                            filterUi = item,
                            onDismissRequest = {
                                openAlertDialog.value = false
                            },
                            selectedItem = { item, id ->
                                onEvent(item, index, false)
                            })
                    }
                }

                is StartFilterUi.FilterStartType.DropDown -> {
                    DropDownSpinner(
                        modifier = modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        label = item.label,
                        defaultText = "Select Country...",
                        selectedItem = item.selected.joinToString(", "),
                        onItemSelected = { id, selected ->
                            onEvent(selected, index, true)
                        },
                        itemList = item.list
                    )
                }

                is StartFilterUi.FilterStartType.Calendar -> {
//                    val calendarState = rememberUseCaseState()
//                    val selectedDateRange = remember {
//                        val value = Range(
//                            java.time.LocalDate.now().minusDays(10),
//                            java.time.LocalDate.now()
//                        )
//                        mutableStateOf(value)
//                    }
//
//                    CalendarDialog(
//                        state = calendarState,
//                        config = CalendarConfig(
//                            style = CalendarStyle.MONTH,
//                        ),
//                        selection = CalendarSelection.Period(
//                            selectedRange = selectedDateRange.value
//                        ) { startDate, endDate ->
//                            val data = startDate.mapToStroke() + " " + endDate.mapToStroke()
//                            onEvent(data, index, true)
//                            selectedDateRange.value = Range(startDate, endDate)
//                        },
//                    )
//                    MenuTextField(
//                        enabled = false,
//                        modifier = modifier
//                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
//                            .clickable {
//                                calendarState.show()
//                            },
//                        label = item.label,
//                        value = item.selected.joinToString(", "),
//                        onValueChange = {}
//                    )
                }
            }


        }
    }
}