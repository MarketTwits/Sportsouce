package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ItemsDialog(
    modifier: Modifier = Modifier,
    values: List<String>,
    label: String,
    selected: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
) = SelectionDialog(modifier, values, label, multiple = false, isSelected = { it == selected },
    onDismissRequest = onDismissRequest, onSelect = {
        onValueChange(it)
        onDismissRequest()
    })

@Composable
internal fun ItemsDialog(
    modifier: Modifier = Modifier,
    values: List<String>,
    label: String,
    selected: List<String>,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
) = SelectionDialog(modifier, values, label, multiple = true, isSelected = { it in selected },
    onDismissRequest = onDismissRequest, onSelect = onValueChange)

internal fun filterSelectionItems(items: List<String>, query: String): List<String> {
    fun normalize(value: String) = value.filterNot(Char::isWhitespace).lowercase().replace('ё', 'е')
    val normalizedQuery = normalize(query)
    return items.filter { normalize(it).contains(normalizedQuery) }.sortedBy { it.lowercase() }
}

@Composable
private fun SelectionDialog(
    modifier: Modifier,
    values: List<String>,
    label: String,
    multiple: Boolean,
    isSelected: (String) -> Boolean,
    onDismissRequest: () -> Unit,
    onSelect: (String) -> Unit,
) {
    var query by rememberSaveable(label) { mutableStateOf("") }
    val filtered = remember(values, query) { filterSelectionItems(values, query) }
    val colors = SauceTheme.colors
    Dialog(onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = modifier.padding(24.dp).widthIn(max = 480.dp).fillMaxWidth().heightIn(max = 560.dp),
            shape = SauceShapes.card,
            color = colors.surfaceRaised,
            contentColor = colors.textPrimary,
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyColumn(
                    modifier = Modifier.weight(1f, fill = false)
                        .then(if (multiple) Modifier else Modifier.selectableGroup()),
                ) {
                    item(key = "selection-header") {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(label, Modifier.weight(1f).semantics { heading() },
                                style = MaterialTheme.typography.titleMedium)
                            IconButton(onClick = onDismissRequest) {
                                Icon(Icons.Default.Close, contentDescription = "Закрыть", modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                    if (values.size > 10) item(key = "selection-search") {
                        OutlinedTextFieldBase(modifier = Modifier.padding(vertical = 8.dp),
                            label = "Поиск", value = query, onValueChange = { query = it })
                    }
                    items(filtered) { item ->
                        val checked = isSelected(item)
                        if (multiple) SauceCheckboxRow(label = item, checked = checked,
                            onCheckedChange = { onSelect(item) })
                        else SauceRadioRow(label = item, selected = checked, onClick = { onSelect(item) })
                    }
                    if (filtered.isEmpty()) item {
                        Text(if (values.isEmpty()) "Список пуст" else "Ничего не найдено",
                            Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            color = colors.textMuted, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                SauceButton(if (multiple) "Готово" else "Отмена", onDismissRequest,
                    variant = SauceButtonVariant.Text)
            }
        }
    }
}
