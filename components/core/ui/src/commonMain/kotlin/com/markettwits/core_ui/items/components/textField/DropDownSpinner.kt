package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.checkbox.CheckBoxBase
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun <E> DropDownSpinner(
    modifier: Modifier = Modifier,
    selectedItem: E,
    onItemSelected: (Int, E) -> Unit,
    itemList: List<E>?,
    enabled: Boolean = true,
    textFiled: @Composable () -> Unit,
) = SelectorMenu(modifier, itemList, enabled, multiple = false,
    isSelected = { selectedItem == it }, onItemSelected = onItemSelected, textFiled = textFiled)

@Composable
fun <E> DropDownSpinnerStage(
    modifier: Modifier = Modifier,
    defaultText: String = "Выберите…",
    selectedItem: List<E>,
    onItemSelected: (Int, E) -> Unit,
    itemList: List<E>?,
    enabled: Boolean = true,
    textFiled: @Composable () -> Unit,
) = SelectorMenu(modifier, itemList, enabled, multiple = true,
    isSelected = { it in selectedItem }, onItemSelected = onItemSelected,
    placeholder = defaultText.takeIf { selectedItem.isEmpty() }, textFiled = textFiled)

@Composable
private fun <E> SelectorMenu(
    modifier: Modifier,
    items: List<E>?,
    enabled: Boolean,
    multiple: Boolean,
    isSelected: (E) -> Boolean,
    onItemSelected: (Int, E) -> Unit,
    placeholder: String? = null,
    textFiled: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(enabled) { if (!enabled) expanded = false }
    var anchorWidth by remember { mutableIntStateOf(0) }
    val minWidth = with(LocalDensity.current) { anchorWidth.toDp() }
    val colors = SauceTheme.colors
    Box(modifier) {
        SelectorFieldAnchor(
            modifier = Modifier.onSizeChanged { anchorWidth = it.width },
            enabled = enabled,
            expanded = expanded && enabled,
            onOpen = { expanded = true },
            placeholder = placeholder,
            trailingIcon = {
                Icon(if (expanded && enabled) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null, modifier = Modifier.size(24.dp),
                    tint = if (enabled) colors.textSecondary else colors.disabledContent)
            },
            content = textFiled,
        )
        DropdownMenu(
            modifier = Modifier.widthIn(min = minWidth),
            containerColor = colors.surfaceRaised,
            shape = SauceShapes.field,
            tonalElevation = 0.dp,
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false },
        ) {
            if (items.isNullOrEmpty()) {
                Text("Список пуст", Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyMedium, color = colors.textMuted)
            }
            items?.forEachIndexed { index, item ->
                val checked = isSelected(item)
                DropdownMenuItem(
                    modifier = Modifier.semantics {
                        if (multiple) {
                            role = Role.Checkbox
                            toggleableState = ToggleableState(checked)
                        } else {
                            role = Role.RadioButton
                            selected = checked
                        }
                    },
                    colors = MenuDefaults.itemColors(textColor = colors.textPrimary),
                    text = { Text(item.toString(), style = MaterialTheme.typography.bodyLarge) },
                    leadingIcon = if (multiple) {
                        { CheckBoxBase(checked = checked, onValueChanged = null) }
                    } else null,
                    trailingIcon = if (!multiple && checked) {
                        { Icon(Icons.Default.Check, contentDescription = null, tint = colors.action) }
                    } else null,
                    onClick = {
                        if (!multiple) expanded = false
                        onItemSelected(index, item)
                    },
                )
            }
        }
    }
}
