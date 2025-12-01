package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.DropDownSpinnerStage
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.start.presentation.result.component.StartMemberResultsComponent
import com.markettwits.sportsouce.start.presentation.result.model.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMemberMemberResultsTopbar(
    state: StartMemberResultsStore.State,
    windowSizeClass: WindowSizeClass,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    component: StartMemberResultsComponent? = null,
    modifier: Modifier = Modifier,
) {
    val isWideScreen = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium

    MemberResultsTopBar(
        modifier = modifier.fillMaxWidth(),
        state = state,
        isWideScreen = isWideScreen,
        onIntent = onIntent,
        component = component
    )
    if (state.isFilterDialogOpen && !isWideScreen) {
        FilterBottomSheet(
            state = state,
            onIntent = onIntent,
            onDismiss = { onIntent(StartMemberResultsStore.Intent.OnToggleFilterDialog) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemberResultsTopBar(
    state: StartMemberResultsStore.State,
    isWideScreen: Boolean,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    component: StartMemberResultsComponent? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StartMemberResultsSearchBar(
                    modifier = Modifier.weight(1f),
                    query = state.filterState.searchQuery,
                    onValueChange = { onIntent(StartMemberResultsStore.Intent.OnChangeQuery(it)) },
                    onClickGoBack = { onIntent(StartMemberResultsStore.Intent.OnClickGoBack) },
                    onClickBrush = {
                        onIntent(StartMemberResultsStore.Intent.OnClickBrushQuery)
                    },
                )

                Spacer(modifier = Modifier.width(8.dp))

                AnimatedVisibility(
                    visible = hasActiveFilters(state.filterState),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    IconButton(
                        onClick = { onIntent(StartMemberResultsStore.Intent.OnClickBrush) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Clear Filters",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                if (!isWideScreen) {
                    BadgedBox(badge = {
                        Badge {
                            if (getActiveFiltersCount(state.filterState) > 0) {
                                Text(
                                    text = getActiveFiltersCount(state.filterState).toString(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }) {
                        IconButton(
                            onClick = { onIntent(StartMemberResultsStore.Intent.OnToggleFilterDialog) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = MaterialTheme.colorScheme.tertiary,
                            )
                        }
                    }
                }
            }

            if (isWideScreen) {
                WideScreenFilters(
                    state = state,
                    onIntent = onIntent,
                )
            }

            val groups = state.filterState.groupFilters.getSelectedGroups()
            val distances = state.filterState.distanceFilters.getSelectDistance()
            val genders = state.filterState.genderFilters.getSelectedGenders()

            SelectedFiltersPane(
                selectedGroups = groups,
                selectedDistance = distances,
                selectedGenders = genders,
                onClickGroup = {
                    onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(it.name))
                },
                onClickDistance = { distance ->
                    onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(distance))
                },
                onClickGender = {
                    onIntent(StartMemberResultsStore.Intent.OnGenderFilterToggle(it.name))
                }
            )
        }
    }
}

@Composable
private fun WideScreenFilters(
    state: StartMemberResultsStore.State,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
        ) {
            DistanceDropDownFilter(
                selectedDistanceFilter = state.filterState.distanceFilters.getSelectDistance(),
                distances = state.filterState.distanceFilters,
                onChoseDistance = {
                    onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(it))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            GroupsDropdownFilter(
                groups = state.filterState.groupFilters,
                onChoseGroupFilter = {
                    onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(it.name))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            GendersDropDownFilter(
                modifier = Modifier,
                genders = state.filterState.genderFilters,
                onChoseGenderFilter = {
                    onIntent(StartMemberResultsStore.Intent.OnGenderFilterToggle(it.name))
                }
            )
        }
    }
}

@Composable
private fun DistanceDropDownFilter(
    modifier: Modifier = Modifier,
    selectedDistanceFilter: DistanceFilter?,
    distances: List<DistanceFilter>,
    onChoseDistance: (DistanceFilter) -> Unit,
) {
    DropDownSpinner(
        itemList = distances.map { it.name },
        selectedItem = selectedDistanceFilter,
        onItemSelected = { id, item ->
            onChoseDistance(distances[id])
        },
        textFiled = {
            OutlinedTextFieldBase(
                modifier = modifier.sizeIn(maxHeight = 60.dp, maxWidth = 170.dp),
                label = "Дистанция",
                value = selectedDistanceFilter?.name ?: "",
                isEnabled = false
            ) {}
        }
    )
}

@Composable
private fun GroupsDropdownFilter(
    modifier: Modifier = Modifier,
    groups: List<GroupFilter>,
    onChoseGroupFilter: (GroupFilter) -> Unit,
) {
    DropDownSpinnerStage(
        selectedItem = groups.getSelectedGroups().map { it.name },
        itemList = groups.map { it.name },
        onItemSelected = { id, item ->
            onChoseGroupFilter(groups[id])
        },
        textFiled = {
            OutlinedTextFieldBase(
                modifier = modifier.sizeIn(maxHeight = 60.dp, maxWidth = 180.dp),
                label = "Группы",
                value = groups.getSelectedGroups().joinToString(", ") { it.name },
                isEnabled = false
            ) {}
        }
    )
}

@Composable
private fun GendersDropDownFilter(
    modifier: Modifier,
    genders: List<GenderFilter>,
    onChoseGenderFilter: (GenderFilter) -> Unit,
) {
    val selectedGender = genders.getSelectedGenders()

    ItemsTextFiledDialog(
        modifier.sizeIn(maxHeight = 60.dp, maxWidth = 180.dp),
        label = "Пол",
        items = genders.map { it.name },
        values = selectedGender.map { it.name },
        onValueChanged = { newValues ->
            val gender = genders.find { it.name == newValues }
            if (gender != null)
                onChoseGenderFilter(gender)
        }
    )
}

@Composable
private fun SelectedFiltersPane(
    modifier: Modifier = Modifier,
    selectedDistance: DistanceFilter?,
    selectedGroups: List<GroupFilter>,
    selectedGenders: List<GenderFilter>,
    onClickGroup: (GroupFilter) -> Unit,
    onClickDistance: (DistanceFilter) -> Unit,
    onClickGender: (GenderFilter) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        maxLines = 3,
    ) {
        selectedDistance?.let { selectedDistance ->
            FilterChipBase(
                selected = true,
                onClick = { onClickDistance(selectedDistance) },
                label = selectedDistance.name
            )
        }
        selectedGroups.forEach { group ->
            FilterChipBase(
                selected = true,
                onClick = { onClickGroup(group) },
                label = group.name
            )
        }
        selectedGenders.forEach { gender ->
            FilterChipBase(
                selected = true,
                onClick = { onClickGender(gender) },
                label = gender.name
            )
        }
    }
}

// Utility functions
private fun hasActiveFilters(filterState: FilterState): Boolean {
    return filterState.searchQuery.isNotBlank() ||
            filterState.distanceFilters.any { it.isSelected } ||
            filterState.groupFilters.any { it.isSelected } ||
            filterState.genderFilters.any { it.isSelected }
}

fun getActiveFiltersCount(filterState: FilterState): Int {
    var count = 0
    if (filterState.searchQuery.isNotBlank()) count++
    count += filterState.distanceFilters.count { it.isSelected }
    count += filterState.groupFilters.count { it.isSelected }
    count += filterState.genderFilters.count { it.isSelected }
    return count
}
