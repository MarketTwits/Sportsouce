package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemsTextFiledDialog(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    items: List<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    LaunchedEffect(enabled) { if (!enabled) openAlertDialog.value = false }

    if (openAlertDialog.value && enabled) {
        ItemsDialog(
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
    SelectorFieldAnchor(modifier = modifier, enabled = enabled, expanded = openAlertDialog.value && enabled,
        onOpen = { openAlertDialog.value = true },
        trailingIcon = { SelectorArrow(expanded = openAlertDialog.value && enabled) }) {
        OutlinedTextFieldBase(
            readOnly = true,
            label = label,
            value = value,
            leadingIcon = leadingIcon,
        ) {
            onValueChanged(it)
        }
    }
}

@Composable
fun ItemsTextFiledDialog(
    modifier: Modifier = Modifier,
    label: String,
    values: List<String>,
    items: List<String>,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    LaunchedEffect(enabled) { if (!enabled) openAlertDialog.value = false }

    if (openAlertDialog.value && enabled) {
        ItemsDialog(
            values = items,
            label = label,
            selected = values,
            onValueChange = {
                onValueChanged(it)
            },
            onDismissRequest = {
                openAlertDialog.value = false
            }
        )
    }
    SelectorFieldAnchor(modifier = modifier, enabled = enabled, expanded = openAlertDialog.value && enabled,
        onOpen = { openAlertDialog.value = true },
        trailingIcon = { SelectorArrow(expanded = openAlertDialog.value && enabled) }) {
        OutlinedTextFieldBase(
            readOnly = true,
            label = label,
            value = values.joinToString(", "),
            leadingIcon = leadingIcon,
        ) {
            onValueChanged(it)
        }
    }
}

@Composable
private fun SelectorArrow(expanded: Boolean) {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
        contentDescription = null,
    )
}
