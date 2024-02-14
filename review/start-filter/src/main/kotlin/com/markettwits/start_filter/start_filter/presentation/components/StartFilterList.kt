package com.markettwits.start_filter.start_filter.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.start_filter.start_filter.presentation.components.dialog.BottomScreenContent


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

                is StartFilterUi.FilterStartType.Calendar -> {}
            }


        }
    }
}