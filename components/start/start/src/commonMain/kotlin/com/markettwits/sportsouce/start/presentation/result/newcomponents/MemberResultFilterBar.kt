//package com.markettwits.sportsouce.start.presentation.result.newcomponents
//
//import GenderResult
//import MemberResultFilterManager
//import StringFilter
//import androidx.compose.animation.*
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import com.markettwits.core_ui.items.window.calculateWindowSizeClass
//import com.markettwits.core_ui.items.window.screenWidthDp
//
//// Main Filter Component
//@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
//@Composable
//fun MemberResultFilterBar(
//    availableDistances: List<String>,
//    availableClubs: List<String>,
//    query : String,
//    modifier: Modifier = Modifier
//) {
//    val configuration = calculateWindowSizeClass()
//
//    val isCompact = configuration.screenWidthDp < 600.dp
//
//    var showFilterSheet by remember { mutableStateOf(false) }
//
//    var searchQuery by remember { mutableStateOf(query) }
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.surface)
//    ) {
//        // Search Bar
//        SearchBar(
//            query = searchQuery,
//            onQueryChange = { query ->
//                searchQuery = query
//            },
//            placeholder = "Поиск по имени/фамилии",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp)
//        )
//
//        if (isCompact) {
//            CompactFilterLayout(
//                filterManager = filterManager,
//                availableDistances = availableDistances,
//                showFilterSheet = showFilterSheet,
//                onShowFilterSheet = { showFilterSheet = it },
//                onFiltersChanged = onFiltersChanged
//            )
//        } else {
//            ExpandedFilterLayout(
//                filterManager = filterManager,
//                availableDistances = availableDistances,
//                availableClubs = availableClubs,
//                onFiltersChanged = onFiltersChanged
//            )
//        }
//
//        // Active Filters Chips
//        ActiveFiltersRow(
//            filterManager = filterManager,
//            onFiltersChanged = onFiltersChanged,
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//        )
//    }
//
//    // Filter Bottom Sheet for Compact Layout
//    if (showFilterSheet) {
//        FilterBottomSheet(
//            filterManager = filterManager,
//            availableDistances = availableDistances,
//            availableClubs = availableClubs,
//            onDismiss = { showFilterSheet = false },
//            onFiltersChanged = {
//                onFiltersChanged()
//                showFilterSheet = false
//            }
//        )
//    }
//}
//
//@Composable
//private fun SearchBar(
//    query: String,
//    onQueryChange: (String) -> Unit,
//    placeholder: String,
//    modifier: Modifier = Modifier
//) {
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    OutlinedTextField(
//        value = query,
//        onValueChange = onQueryChange,
//        placeholder = {
//            Text(
//                text = placeholder,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//            )
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Default.Search,
//                contentDescription = "Search",
//                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//            )
//        },
//        trailingIcon = if (query.isNotEmpty()) {
//            {
//                IconButton(onClick = { onQueryChange("") }) {
//                    Icon(
//                        imageVector = Icons.Default.Clear,
//                        contentDescription = "Clear search",
//                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                }
//            }
//        } else null,
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//        keyboardActions = KeyboardActions(
//            onSearch = { keyboardController?.hide() }
//        ),
//        singleLine = true,
//        shape = RoundedCornerShape(24.dp),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = MaterialTheme.colorScheme.primary,
//            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
//        ),
//        modifier = modifier
//    )
//}
//
//@Composable
//private fun CompactFilterLayout(
//    availableDistances: List<String>,
//    showFilterSheet: Boolean,
//    onShowFilterSheet: (Boolean) -> Unit,
//    onFiltersChanged: () -> Unit
//) {
//    LazyRow(
//        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        // Distance Dropdown (Always Visible)
//        item {
//            DistanceFilterChip(
//                filterManager = filterManager,
//                availableDistances = availableDistances,
//                onFiltersChanged = onFiltersChanged
//            )
//        }
//
//        // Filter Button
//        item {
//            val activeCount = filterManager.getActiveFiltersCount() - 1 // Exclude distance
//
//            FilterChip(
//                selected = activeCount > 0,
//                onClick = { onShowFilterSheet(true) },
//                label = {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.FilterList,
//                            contentDescription = null,
//                            modifier = Modifier.size(16.dp)
//                        )
//                        Text("Фильтры")
//                        if (activeCount > 0) {
//                            Badge(
//                                modifier = Modifier.size(16.dp),
//                                containerColor = MaterialTheme.colorScheme.primary
//                            ) {
//                                Text(
//                                    text = activeCount.toString(),
//                                    style = MaterialTheme.typography.labelSmall,
//                                    color = MaterialTheme.colorScheme.onPrimary
//                                )
//                            }
//                        }
//                    }
//                }
//            )
//        }
//    }
//}
//
//@Composable
//private fun ExpandedFilterLayout(
//    selectedDistance: String,
//    availableDistances: List<String>,
//    availableClubs: List<String>,
//    onDistanceFilterChanged: (String) -> Unit
//) {
//    LazyRow(
//        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        item {
//            DistanceFilterChip(
//                selectedDistance = selectedDistance,
//                availableDistances = availableDistances,
//                onFiltersChanged = onDistanceFilterChanged
//            )
//        }
//
//        item {
//            GenderFilterChip(
//                onFiltersChanged = onFiltersChanged
//            )
//        }
//
//        item {
//            StringFilterChip(
//                label = "Клуб",
//                filter = filterManager.clubFilter,
//                availableOptions = availableClubs,
//                onFiltersChanged = onFiltersChanged
//            )
//        }
//    }
//}
//
//@Composable
//private fun DistanceFilterChip(
//    availableDistances: List<String>,
//    selectedDistance : String,
//    onFiltersChanged: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val currentDistance = selectedDistance
//
//    Box {
//        FilterChip(
//            selected = availableDistances.contains(selectedDistance),
//            onClick = { expanded = true },
//            label = {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Text(currentDistance ?: "Дистанция")
//                    Icon(
//                        imageVector = Icons.Default.ArrowDropDown,
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//            }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            availableDistances.forEach { distance ->
//                DropdownMenuItem(
//                    text = { Text(distance) },
//                    onClick = {
//                        expanded = false
//                        onFiltersChanged(distance)
//                    },
//                    leadingIcon = if (currentDistance == distance) {
//                        { Icon(Icons.Default.Check, contentDescription = null) }
//                    } else null
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun GenderFilterChip(
//    selectedGender: String,
//    genders: List<String>,
//    onFiltersChanged: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Box {
//        FilterChip(
//            selected = genders.contains(selectedGender),
//            onClick = { expanded = true },
//            label = {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Text(selectedGender ?: "Пол")
//                    Icon(
//                        imageVector = Icons.Default.ArrowDropDown,
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//            }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            genders.forEach { gender ->
//                DropdownMenuItem(
//                    text = { Text(gender) },
//                    onClick = {
//                        expanded = false
//                        onFiltersChanged(gender)
//                    },
//                    leadingIcon = if (selectedGender == gender || (selectedGender == null && gender == selectedGender.ALL)) {
//                        { Icon(Icons.Default.Check, contentDescription = null) }
//                    } else null
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun StringFilterChip(
//    label: String,
//    filter: StringFilter,
//    availableOptions: List<String>,
//    onFiltersChanged: () -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val currentValue = filter.currentValue
//
//    Box {
//        FilterChip(
//            selected = filter.isActive(),
//            onClick = { expanded = true },
//            label = {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Text(
//                        text = currentValue?.take(10) ?: label,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    Icon(
//                        imageVector = Icons.Default.ArrowDropDown,
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//            }
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            DropdownMenuItem(
//                text = { Text("Все") },
//                onClick = {
//                    filter.reset()
//                    expanded = false
//                    onFiltersChanged()
//                },
//                leadingIcon = if (!filter.isActive()) {
//                    { Icon(Icons.Default.Check, contentDescription = null) }
//                } else null
//            )
//
//            availableOptions.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option) },
//                    onClick = {
//                        filter.setValue(option)
//                        expanded = false
//                        onFiltersChanged()
//                    },
//                    leadingIcon = if (currentValue == option) {
//                        { Icon(Icons.Default.Check, contentDescription = null) }
//                    } else null
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun ActiveFiltersRow(
//    filterManager: MemberResultFilterManager,
//    onFiltersChanged: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val activeFilters = remember(filterManager.getActiveFiltersCount()) {
//        mutableListOf<Pair<String, () -> Unit>>().apply {
//            filterManager.genderFilter.currentValue?.let { gender ->
//                if (gender != GenderResult.ALL) {
//                    add(gender.displayName to {
//                        filterManager.genderFilter.reset()
//                        onFiltersChanged()
//                    })
//                }
//            }
//            filterManager.clubFilter.currentValue?.let { club ->
//                add("Клуб: $club" to {
//                    filterManager.clubFilter.reset()
//                    onFiltersChanged()
//                })
//            }
//            filterManager.cityFilter.currentValue?.let { city ->
//                add("Город: $city" to {
//                    filterManager.cityFilter.reset()
//                    onFiltersChanged()
//                })
//            }
//            filterManager.nameSearchFilter.currentValue?.let { search ->
//                add("\"$search\"" to {
//                    filterManager.nameSearchFilter.reset()
//                    onFiltersChanged()
//                })
//            }
//        }
//    }
//
//    AnimatedVisibility(
//        visible = activeFilters.isNotEmpty(),
//        enter = slideInVertically() + fadeIn(),
//        exit = slideOutVertically() + fadeOut()
//    ) {
//        LazyRow(
//            modifier = modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(activeFilters) { (label, onRemove) ->
//                AssistChip(
//                    onClick = onRemove,
//                    label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
//                    trailingIcon = {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "Remove filter",
//                            modifier = Modifier.size(16.dp)
//                        )
//                    },
//                    colors = AssistChipDefaults.assistChipColors(
//                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
//                    )
//                )
//            }
//
//            if (activeFilters.size > 1) {
//                item {
//                    AssistChip(
//                        onClick = {
//                            filterManager.resetAllExceptDistance()
//                            onFiltersChanged()
//                        },
//                        label = { Text("Очистить все") },
//                        colors = AssistChipDefaults.assistChipColors(
//                            containerColor = MaterialTheme.colorScheme.errorContainer,
//                            labelColor = MaterialTheme.colorScheme.onErrorContainer
//                        )
//                    )
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun FilterBottomSheet(
//    filterManager: MemberResultFilterManager,
//    availableDistances: List<String>,
//    availableClubs: List<String>,
//    onDismiss: () -> Unit,
//    onFiltersChanged: () -> Unit
//) {
//    val bottomSheetState = rememberModalBottomSheetState()
//
//    ModalBottomSheet(
//        onDismissRequest = onDismiss,
//        sheetState = bottomSheetState
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(
//                text = "Фильтры",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//
//            GenderFilterSection(
//                filterManager = filterManager,
//                onFiltersChanged = onFiltersChanged
//            )
//
//            StringFilterSection(
//                title = "Клуб",
//                filter = filterManager.clubFilter,
//                availableOptions = availableClubs,
//                onFiltersChanged = onFiltersChanged
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                OutlinedButton(
//                    onClick = {
//                        filterManager.resetAllExceptDistance()
//                        onFiltersChanged()
//                    },
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Сбросить")
//                }
//
//                Button(
//                    onClick = onFiltersChanged,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text("Применить")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//
//@Composable
//private fun GenderFilterSection(
//    filterManager: MemberResultFilterManager,
//    onFiltersChanged: () -> Unit
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Text(
//            text = "Пол",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Medium
//        )
//
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            GenderResult.entries.forEach { gender ->
//                val isSelected = filterManager.genderFilter.currentValue == gender ||
//                    (filterManager.genderFilter.currentValue == null && gender == GenderResult.ALL)
//
//                FilterChip(
//                    selected = isSelected,
//                    onClick = {
//                        filterManager.genderFilter.setValue(if (gender == GenderResult.ALL) null else gender)
//                        onFiltersChanged()
//                    },
//                    label = { Text(gender.displayName) }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun StringFilterSection(
//    title: String,
//    filter: StringFilter,
//    availableOptions: List<String>,
//    onFiltersChanged: () -> Unit
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Text(
//            text = title,
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Medium
//        )
//
//        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            item {
//                FilterChip(
//                    selected = !filter.isActive(),
//                    onClick = {
//                        filter.reset()
//                        onFiltersChanged()
//                    },
//                    label = { Text("Все") }
//                )
//            }
//
//            items(availableOptions) { option ->
//                FilterChip(
//                    selected = filter.currentValue == option,
//                    onClick = {
//                        filter.setValue(option)
//                        onFiltersChanged()
//                    },
//                    label = { Text(option, maxLines = 1, overflow = TextOverflow.Ellipsis) }
//                )
//            }
//        }
//    }
//}