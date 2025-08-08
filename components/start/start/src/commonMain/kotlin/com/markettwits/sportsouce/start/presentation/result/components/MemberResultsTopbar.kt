package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.textField.DropDownSpinner
import com.markettwits.core_ui.items.components.textField.DropDownSpinnerStage
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.presentation.result.model.*
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartMemberMemberResultsTopbar(
    state: StartMemberResultsStore.State,
    windowSizeClass: WindowSizeClass,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isWideScreen = windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Medium

    MemberResultsTopBar(
        modifier = modifier.fillMaxWidth(),
        state = state,
        isWideScreen = isWideScreen,
        onIntent = onIntent
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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StartMemberResultsSearchBar(
                    modifier = Modifier.weight(1f),
                    query = state.filterState.searchQuery,
                    onValueChange = { onIntent(StartMemberResultsStore.Intent.OnChangeQuery(it)) },
                    onClickGoBack = { onIntent(StartMemberResultsStore.Intent.OnClickGoBack) },
                    onClickBrush = {
                        onIntent(StartMemberResultsStore.Intent.OnClickBrushQuery)
                    }
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

                SortButton(
                    sortBy = state.filterState.sortBy,
                    sortOrder = state.filterState.sortOrder,
                    onSortByChange = { onIntent(StartMemberResultsStore.Intent.OnSortByChange(it)) },
                    onSortOrderChange = { onIntent(StartMemberResultsStore.Intent.OnSortOrderChange(it)) }
                )

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

            val teams = state.filterState.teamFilters.getSelectTeams()
            val groups = state.filterState.groupFilters.getSelectedGroups()
            val distances = state.filterState.distanceFilters.getSelectDistance()

            SelectedFiltersPane(
                selectedTeams = teams,
                selectedGroups = groups,
                selectedDistance = distances,
                onClickTeam = {
                    onIntent(StartMemberResultsStore.Intent.OnTeamFilterToggle(it.name))
                },
                onClickGroup = {
                    onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(it.name))
                },
                onClickDistance = { distance ->
                    onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(distance))
                }
            )
        }
    }
}

@Composable
private fun SortButton(
    sortBy: SortBy,
    sortOrder: SortOrder,
    onSortByChange: (SortBy) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { showSortMenu = true }
        ) {
            Icon(
                imageVector = if (sortOrder == SortOrder.ASC) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                contentDescription = "Sort",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        DropdownMenu(
            expanded = showSortMenu,
            onDismissRequest = { showSortMenu = false },
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                Text(
                    text = "Сортировать по :",
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                SortBy.entries.forEach { sort ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    sort.displayName,
                                    fontFamily = FontNunito.semiBoldBold(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 14.sp,
                                )
                                if (sortBy == sort) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            if (sortBy == sort) {
                                onSortOrderChange(if (sortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC)
                            } else {
                                onSortByChange(sort)
                            }
                            showSortMenu = false
                        }
                    )
                }
            }
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
            TeamsDropDownFilter(
                modifier = Modifier,
                teams = state.filterState.teamFilters,
                onChoseTeamFilter = {
                    onIntent(StartMemberResultsStore.Intent.OnTeamFilterToggle(it.name))
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
private fun TeamsDropDownFilter(
    modifier: Modifier,
    teams: List<TeamFilter>,
    onChoseTeamFilter: (TeamFilter) -> Unit,
) {
    val selectedTeam = teams.getSelectTeams()

    ItemsTextFiledDialog(
        modifier.sizeIn(maxHeight = 60.dp, maxWidth = 180.dp),
        label = "Команды",
        items = teams.map { it.name },
        values = selectedTeam.map { it.name },
        onValueChanged = { newValues ->
            val team = teams.find { it.name == newValues }
            if (team != null)
                onChoseTeamFilter(team)
        }
    )
}

@Composable
private fun SelectedFiltersPane(
    modifier: Modifier = Modifier,
    selectedTeams: List<TeamFilter>,
    selectedDistance: DistanceFilter?,
    selectedGroups: List<GroupFilter>,
    onClickTeam: (TeamFilter) -> Unit,
    onClickGroup: (GroupFilter) -> Unit,
    onClickDistance: (DistanceFilter) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        maxLines = 3,
    ) {
        selectedDistance?.let { selectedDistance ->
            DefaultFilterChip(
                selected = true,
                onClick = { onClickDistance(selectedDistance) },
                label = selectedDistance.name
            )
        }
        selectedGroups.forEach { group ->
            DefaultFilterChip(
                selected = true,
                onClick = { onClickGroup(group) },
                label = group.name
            )
        }
        selectedTeams.forEach { team ->
            DefaultFilterChip(
                selected = true,
                onClick = { onClickTeam(team) },
                label = team.name
            )
        }
    }
}

@Composable
private fun DefaultFilterChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontFamily = FontNunito.medium(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        },
        leadingIcon = {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.secondary,
        )
    )
}


// Utility functions
private fun hasActiveFilters(filterState: FilterState): Boolean {
    return filterState.searchQuery.isNotBlank() ||
            filterState.distanceFilters.any { it.isSelected } ||
            filterState.groupFilters.any { it.isSelected } ||
            filterState.teamFilters.any { it.isSelected }
}

fun getActiveFiltersCount(filterState: FilterState): Int {
    var count = 0
    if (filterState.searchQuery.isNotBlank()) count++
    count += filterState.distanceFilters.count { it.isSelected }
    count += filterState.groupFilters.count { it.isSelected }
    count += filterState.teamFilters.count { it.isSelected }
    return count
}