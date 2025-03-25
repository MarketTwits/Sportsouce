package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.filter.presentation.components.dialog.BottomScreenContent


@Composable
fun StartFilterList(
    modifier: Modifier = Modifier,
    startFilter: StartFilterUi,
    onEvent: (String, Int, Boolean) -> Unit
) {
    Column {
        startFilter.items.forEachIndexed { index, item ->
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
                    if (openAlertDialog.value) {
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
                        itemList = item.list,
                        selectedItem = item.selected.joinToString(", "),
                        onItemSelected = { id, item ->
                            onEvent(item, index, true)
                        },
                        textFiled = {
                            OutlinedTextFieldBase(
                                modifier = modifier,
                                label = item.label,
                                value = item.selected.joinToString(", "),
                                isEnabled = false
                            ) {}
                        }
                    )
                }

                is StartFilterUi.FilterStartType.Calendar -> {}
            }
        }
    }
}