package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.sportsouce.start.presentation.common.components.StartListFilterSection
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreenComponent
import com.markettwits.sportsouce.start.presentation.membres.models.*

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@Composable
fun StartMembersFilterDialog(component: StartMembersScreenComponent) {
    val visible by component.showFilterDialog.subscribeAsState()
    val state by component.state.collectAsState()
    if (visible) DefaultModalBottomSheet(title = "Фильтры участников", onDismissRequest = component::closeFilter) {
        MembersFilterContent(
            groups = state.selectedFiltersUi,
            onToggle = component::toggleFilter,
            onClear = {
                component.updateFilter(state.selectedFiltersUi.map { group ->
                    if (group.title == "Пол" || group.title == "Дистанция") group.copy(items = group.items.map { MembersFilterItem.Base(it.title) }) else group
                })
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MembersFilterContent(groups: List<MembersFilterGroup>, onToggle: (String, String) -> Unit, onClear: () -> Unit) {
    val visibleGroups = groups.filter { it.title == "Дистанция" || it.title == "Пол" }
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
        if (visibleGroups.any { group -> group.items.any { it is MembersFilterItem.Selected } }) {
            SauceButton("Сбросить фильтры", onClear, variant = SauceButtonVariant.Text, fillWidth = false)
        }
        LazyColumn(Modifier.weight(1f, fill = false), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            visibleGroups.forEach { group ->
                item(key = group.title) {
                    StartListFilterSection(group.title) {
                        FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            group.items.forEach { item ->
                                FilterChipBase(selected = item is MembersFilterItem.Selected,
                                    onClick = { onToggle(group.title, item.title) }, label = item.title)
                            }
                        }
                    }
                }
            }
        }
    }
}
