package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.toolbar.CollapsingToolbarScaffoldScopeInstance.align
import com.markettwits.core_ui.items.components.toolbar.ExperimentalToolbarApi
import com.markettwits.core_ui.items.extensions.noRippleClickable

@Composable
fun ItemsTextFiledDialog(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    items: List<String>,
    onValueChanged: (String) -> Unit,
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

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun ItemsTextFiledDialog(
    modifier: Modifier = Modifier,
    label: String,
    values: List<String>,
    items: List<String>,
    onValueChanged: (String) -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
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
    OutlinedTextFieldBase(
        modifier = modifier
            .noRippleClickable() { openAlertDialog.value = true },
        isEnabled = false,
        label = label,
        value = values.joinToString(", "),
        trailingIcon = {
            androidx.compose.material3.Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.tertiary,
                imageVector = if (items.isNotEmpty()) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                contentDescription = "Close"
            )
        }
    ) {
        onValueChanged(it)
    }
}