package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.sportsouce.start.presentation.common.components.StartListFilterSection
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore

@Composable
fun FilterBottomSheet(state: StartMemberResultsStore.State, onIntent: (StartMemberResultsStore.Intent) -> Unit,
    onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    DefaultModalBottomSheet(modifier = modifier, title = "Фильтры результатов", onDismissRequest = onDismiss) {
        ResultFiltersContent(state, onIntent)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ResultFiltersContent(state: StartMemberResultsStore.State, onIntent: (StartMemberResultsStore.Intent) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
        if (getActiveFiltersCount(state.filterState) > 0) SauceButton("Сбросить фильтры",
            { onIntent(StartMemberResultsStore.Intent.OnClickBrush) }, variant = SauceButtonVariant.Text, fillWidth = false)
        LazyColumn(Modifier.weight(1f, fill = false), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            item {
                StartListFilterSection("Дистанция") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        state.filterState.distanceFilters.forEach { distance ->
                            FilterChipBase(selected = distance.isSelected, label = distance.name,
                                onClick = { onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(distance)) })
                        }
                    }
                }
            }
            item {
                StartListFilterSection("Группы") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        state.filterState.groupFilters.forEach { group ->
                            FilterChipBase(selected = group.isSelected, label = group.name,
                                onClick = { onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(group.name)) })
                        }
                    }
                }
            }
            item {
                StartListFilterSection("Пол") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        state.filterState.genderFilters.forEach { gender ->
                            FilterChipBase(selected = gender.isSelected, label = gender.name,
                                onClick = { onIntent(StartMemberResultsStore.Intent.OnGenderFilterToggle(gender.name)) })
                        }
                    }
                }
            }
        }
    }
}
