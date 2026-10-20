package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi

@Composable
fun StartFilterList(
    modifier: Modifier = Modifier,
    startFilter: StartFilterUi,
    onEvent: (String, Int, Boolean) -> Unit,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        startFilter.items.forEachIndexed { index, item ->
            when (item.type) {
                StartFilterUi.FilterStartType.Dialog -> ItemsTextFiledDialog(
                    label = item.label,
                    values = item.selected,
                    items = item.list,
                    onValueChanged = { onEvent(it, index, false) },
                )
                StartFilterUi.FilterStartType.DropDown -> DropDownSpinner(
                    itemList = item.list,
                    selectedItem = item.selected.joinToString(", "),
                    onItemSelected = { _, selected -> onEvent(selected, index, true) },
                    textFiled = {
                        OutlinedTextFieldBase(
                            label = item.label,
                            value = item.selected.joinToString(", "),
                            readOnly = true,
                            onValueChange = {},
                        )
                    },
                )
                StartFilterUi.FilterStartType.Calendar -> Unit
            }
        }
    }
}
