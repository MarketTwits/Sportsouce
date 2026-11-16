package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi.Key
import com.markettwits.sportsouce.start.search.filter.presentation.store.StartFilterStore.Intent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
internal fun StartFilterList(filter: StartFilterUi, onEvent: (Intent) -> Unit) {
    fun pick(key: Key, value: String, single: Boolean = true) {
        val group = filter.item(key) ?: return
        onEvent(Intent.OnItemSelected(value, filter.items.indexOf(group), single))
    }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        FilterSection("Место и организатор") {
            filter.item(Key.City)?.let { item ->
                SingleFilterField(item, "Город", "Все города", Icons.Outlined.LocationOn) { pick(Key.City, it) }
            }
            filter.item(Key.Organizer)?.let { item ->
                SingleFilterField(item, "Организатор", "Все организаторы", Icons.Outlined.Groups) { pick(Key.Organizer, it) }
            }
        }

        FilterSection("Период старта") {
            StartDateFields(filter, onEvent)
            filter.item(Key.Status)?.let { item ->
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChipBase(selected = item.selected.isEmpty(), label = "Все старты", onClick = { pick(Key.Status, "") })
                    item.list.forEach { option ->
                        FilterChipBase(selected = option in item.selected, label = option, onClick = { pick(Key.Status, option) })
                    }
                }
            }
        }

        FilterSection("Спорт и сезон") {
            filter.item(Key.Sport)?.let { item ->
                ItemsTextFiledDialog(label = item.label, values = item.selected, items = item.list,
                    leadingIcon = { FilterIcon(Icons.Outlined.EmojiEvents) }, placeholder = "Все виды спорта",
                    onValueChanged = { pick(Key.Sport, it, single = false) })
            }
            filter.item(Key.Season)?.let { item ->
                ItemsTextFiledDialog(label = item.label, values = item.selected, items = item.list,
                    leadingIcon = { FilterIcon(Icons.Outlined.CalendarMonth) }, placeholder = "Все сезоны",
                    onValueChanged = { pick(Key.Season, it, single = false) })
            }
        }

        filter.item(Key.Sort)?.let { item ->
            val selected = item.selected.firstOrNull() ?: "По популярности"
            DropDownSpinner(itemList = item.list, selectedItem = selected,
                onItemSelected = { _, option ->
                    pick(Key.Sort, if (option == "По популярности") "" else option)
                }, textFiled = {
                    OutlinedTextFieldBase(label = "Сортировка", value = selected, readOnly = true, onValueChange = {})
                })
        }
    }
}

@Composable
private fun SingleFilterField(
    item: StartFilterUi.FilterGroupItemUi,
    label: String,
    any: String,
    icon: ImageVector,
    select: (String) -> Unit,
) {
    ItemsTextFiledDialog(
        label = label,
        value = item.selected.firstOrNull() ?: any,
        items = listOf(any) + item.list,
        leadingIcon = { FilterIcon(icon) },
        onValueChanged = { select(it.takeUnless { value -> value == any }.orEmpty()) },
    )
}

@Composable
private fun FilterSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, Modifier.semantics { heading() }, style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
private fun StartDateFields(filter: StartFilterUi, onEvent: (Intent) -> Unit) {
    val from: @Composable (Modifier) -> Unit = { modifier ->
        StartDateField("Дата начала", filter.fromDate, modifier, filter.hasInvalidPeriod) { onEvent(Intent.SetFromDate(it)) }
    }
    val to: @Composable (Modifier) -> Unit = { modifier ->
        StartDateField("Дата окончания", filter.toDate, modifier, filter.hasInvalidPeriod) { onEvent(Intent.SetToDate(it)) }
    }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        if (maxWidth >= 320.dp * LocalDensity.current.fontScale) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                from(Modifier.weight(1f))
                DateRangeSeparator()
                to(Modifier.weight(1f))
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                from(Modifier.fillMaxWidth())
                DateRangeSeparator()
                to(Modifier.fillMaxWidth())
            }
        }
    }
    if (filter.hasInvalidPeriod) {
        Text("Дата окончания должна быть не раньше начала", color = SauceTheme.colors.danger.content,
            style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun DateRangeSeparator() {
    Text(
        text = "–",
        modifier = Modifier.clearAndSetSemantics {},
        color = SauceTheme.colors.textSecondary,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun StartDateField(
    label: String,
    date: LocalDate?,
    modifier: Modifier,
    isError: Boolean,
    select: (LocalDate?) -> Unit,
) {
    val value = date?.filterDateLabel().orEmpty()
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    CalendarTextFiled(
        modifier = modifier.semantics { contentDescription = label },
        value = value, yearRange = 1925..(currentYear + 10), clearable = true,
        textFiled = { fieldModifier ->
            OutlinedTextFieldBase(
                modifier = fieldModifier, label = "", value = value, readOnly = true, isError = isError,
                placeholder = { Text("Любая дата") },
                trailingIcon = { FilterIcon(Icons.Outlined.CalendarToday) },
                onValueChange = {},
            )
        },
        onValueChanged = { text ->
            select(text.takeIf(String::isNotEmpty)?.let { LocalDate.parse(it.split('.').asReversed().joinToString("-")) })
        },
    )
}

internal fun LocalDate.filterDateLabel(): String = toString().split('-').asReversed().joinToString(".")

@Composable
private fun FilterIcon(icon: ImageVector) {
    Icon(icon, contentDescription = null, tint = SauceTheme.colors.textSecondary, modifier = Modifier.size(20.dp))
}
