package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ItemsTextFiledDialog(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    items: List<String>,
    onValueChanged: (String) -> Unit
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        ItemsDialog(
            modifier = modifier,
            values = items,
            label = label,
            selected = value,
            onValueChange = {
                onValueChanged(it)
            },
            onDismissRequest = {
                openAlertDialog.value = false
            }
        )
    }
    OutlinedTextFieldBase(
        modifier = modifier.clickable {
            openAlertDialog.value = true
        },
        isEnabled = false,
        label = label,
        value = value
    ) {
        onValueChanged(it)
    }
}