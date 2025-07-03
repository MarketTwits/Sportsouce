package com.markettwits.sportsouce.start.presentation.result.newcomponents

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.markettwits.sportsouce.bottom_bar.component.storage.BottomBarStorageImpl.state
import com.markettwits.sportsouce.start.presentation.result.model.DistanceFilter
import com.markettwits.sportsouce.start.presentation.result.model.FilterState
import com.markettwits.sportsouce.start.presentation.result.model.GenderFilter
import com.markettwits.sportsouce.start.presentation.result.model.GroupFilter
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.result.model.SortBy
import com.markettwits.sportsouce.start.presentation.result.model.SortOrder
import com.markettwits.sportsouce.start.presentation.result.model.TeamFilter
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberResultsTopbar(
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
        FilterDialog(
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
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column {
            // Основная строка с навигацией и поиском
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Кнопка назад
                IconButton(
                    onClick = { onIntent(StartMemberResultsStore.Intent.OnClickGoBack) }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }

                // Поле поиска
                SearchBar(
                    query = state.filterState.searchQuery,
                    onQueryChange = { onIntent(StartMemberResultsStore.Intent.OnChangeQuery(it)) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Кнопка очистки фильтров
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Кнопка сортировки
                SortButton(
                    sortBy = state.filterState.sortBy,
                    sortOrder = state.filterState.sortOrder,
                    onSortByChange = { onIntent(StartMemberResultsStore.Intent.OnSortByChange(it)) },
                    onSortOrderChange = { onIntent(StartMemberResultsStore.Intent.OnSortOrderChange(it)) }
                )

                // Кнопка фильтров для узких экранов
                if (!isWideScreen) {
                    IconButton(
                        onClick = { onIntent(StartMemberResultsStore.Intent.OnToggleFilterDialog) }
                    ) {
                        Badge(
                            modifier = Modifier.offset(x = 8.dp, y = (-8).dp)
                        ) {
                            if (getActiveFiltersCount(state.filterState) > 0) {
                                Text(
                                    text = getActiveFiltersCount(state.filterState).toString(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filters"
                        )
                    }
                }
            }

            // Фильтры для широких экранов
            if (isWideScreen) {
                WideScreenFilters(
                    state = state,
                    onIntent = onIntent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .focusRequester(focusRequester)
            .height(56.dp),
        placeholder = {
            Text(
                text = "Search members...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = RoundedCornerShape(28.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
        ),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
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
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = showSortMenu,
            onDismissRequest = { showSortMenu = false }
        ) {
            SortBy.values().forEach { sort ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(sort.displayName)
                            if (sortBy == sort) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
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

@Composable
private fun WideScreenFilters(
    state: StartMemberResultsStore.State,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        // Фильтр по полу
        item {
            GenderFilterChip(
                selectedGender = state.filterState.genderFilter,
                onGenderChange = { onIntent(StartMemberResultsStore.Intent.OnGenderFilterChange(it)) }
            )
        }

        // Фильтры по дистанции
        items(state.filterState.distanceFilters) { distance ->
            FilterChip(
                selected = distance.isSelected,
                onClick = { onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(distance.name)) },
                label = { Text(distance.name) },
                leadingIcon = if (distance.isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null
            )
        }

        // Фильтры по группам
        items(state.filterState.groupFilters) { group ->
            FilterChip(
                selected = group.isSelected,
                onClick = { onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(group.name)) },
                label = { Text(group.name) },
                leadingIcon = if (group.isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null
            )
        }

        // Фильтры по командам
        items(state.filterState.teamFilters) { team ->
            FilterChip(
                selected = team.isSelected,
                onClick = { onIntent(StartMemberResultsStore.Intent.OnTeamFilterToggle(team.name)) },
                label = { Text(team.name) },
                leadingIcon = if (team.isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun GenderFilterChip(
    selectedGender: GenderFilter,
    onGenderChange: (GenderFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showGenderMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FilterChip(
            selected = selectedGender != GenderFilter.ALL,
            onClick = { showGenderMenu = true },
            label = { Text(selectedGender.displayName) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        )

        DropdownMenu(
            expanded = showGenderMenu,
            onDismissRequest = { showGenderMenu = false }
        ) {
            GenderFilter.values().forEach { gender ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(gender.displayName)
                            if (selectedGender == gender) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        onGenderChange(gender)
                        showGenderMenu = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterDialog(
    state: StartMemberResultsStore.State,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Заголовок
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filters",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onDismiss
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Контент фильтров
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    // Фильтр по полу
                    item {
                        FilterSection(
                            title = "Gender",
                            icon = Icons.Default.Person
                        ) {
                            GenderFilterSection(
                                selectedGender = state.filterState.genderFilter,
                                onGenderChange = { onIntent(StartMemberResultsStore.Intent.OnGenderFilterChange(it)) }
                            )
                        }
                    }

                    // Фильтры по дистанции
                    if (state.filterState.distanceFilters.isNotEmpty()) {
                        item {
                            FilterSection(
                                title = "Distance",
                                icon = Icons.Default.DirectionsRun
                            ) {
                                MultiSelectFilterSection(
                                    items = state.filterState.distanceFilters,
                                    onItemToggle = { onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(it)) }
                                )
                            }
                        }
                    }

                    // Фильтры по группам
                    if (state.filterState.groupFilters.isNotEmpty()) {
                        item {
                            FilterSection(
                                title = "Groups",
                                icon = Icons.Default.Group
                            ) {
                                MultiSelectFilterSection(
                                    items = state.filterState.groupFilters,
                                    onItemToggle = { onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(it)) }
                                )
                            }
                        }
                    }

                    // Фильтры по командам
                    if (state.filterState.teamFilters.isNotEmpty()) {
                        item {
                            FilterSection(
                                title = "Teams",
                                icon = Icons.Default.Groups
                            ) {
                                MultiSelectFilterSection(
                                    items = state.filterState.teamFilters,
                                    onItemToggle = { onIntent(StartMemberResultsStore.Intent.OnTeamFilterToggle(it)) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Кнопки действий
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onIntent(StartMemberResultsStore.Intent.OnClearFilters) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear All")
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        content()
    }
}

@Composable
private fun GenderFilterSection(
    selectedGender: GenderFilter,
    onGenderChange: (GenderFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(GenderFilter.values()) { gender ->
            FilterChip(
                selected = selectedGender == gender,
                onClick = { onGenderChange(gender) },
                label = { Text(gender.displayName) }
            )
        }
    }
}

@Composable
private fun <T : Any> MultiSelectFilterSection(
    items: List<T>,
    onItemToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowColumn(
        maxLines = 2,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            val name = when (item) {
                is GroupFilter -> item.name
                is TeamFilter -> item.name
                is DistanceFilter -> item.name
                else -> item.toString()
            }
            val isSelected = when (item) {
                is GroupFilter -> item.isSelected
                is TeamFilter -> item.isSelected
                is DistanceFilter -> item.isSelected
                else -> false
            }

            FilterChip(
                selected = isSelected,
                onClick = { onItemToggle(name) },
                label = {
                    Text(
                        text = name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Utility functions
private fun hasActiveFilters(filterState: FilterState): Boolean {
    return filterState.searchQuery.isNotBlank() ||
            filterState.genderFilter != GenderFilter.ALL ||
            filterState.distanceFilters.any { it.isSelected } ||
            filterState.groupFilters.any { it.isSelected } ||
            filterState.teamFilters.any { it.isSelected }
}

private fun getActiveFiltersCount(filterState: FilterState): Int {
    var count = 0
    if (filterState.searchQuery.isNotBlank()) count++
    if (filterState.genderFilter != GenderFilter.ALL) count++
    count += filterState.distanceFilters.count { it.isSelected }
    count += filterState.groupFilters.count { it.isSelected }
    count += filterState.teamFilters.count { it.isSelected }
    return count
}