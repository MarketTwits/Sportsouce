package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.presentation.result.model.DistanceFilter
import com.markettwits.sportsouce.start.presentation.result.model.GenderFilter
import com.markettwits.sportsouce.start.presentation.result.model.GroupFilter
import com.markettwits.sportsouce.start.presentation.result.model.getSelectDistance
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    state: StartMemberResultsStore.State,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        dragHandle = {
            Surface(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(32.dp)
                    .height(4.dp),
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f)
            ) {}
        }
    ) {
        FilterBottomSheetContent(
            state = state,
            onIntent = onIntent,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    state: StartMemberResultsStore.State,
    onIntent: (StartMemberResultsStore.Intent) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        FilterBottomSheetHeader(
            activeFiltersCount = getActiveFiltersCount(state.filterState),
            onDismiss = onDismiss,
            onClearAll = { onIntent(StartMemberResultsStore.Intent.OnClickBrush) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                FilterSection(
                    title = "Дистанция",
                    icon = Icons.Default.LocationOn
                ) {
                    MobileDistanceFilter(
                        selectedDistance = state.filterState.distanceFilters.getSelectDistance(),
                        distances = state.filterState.distanceFilters,
                        onDistanceSelected = { distance ->
                            onIntent(StartMemberResultsStore.Intent.OnDistanceFilterToggle(distance))
                        }
                    )
                }
            }

            // Группы
            item {
                FilterSection(
                    title = "Группы",
                    icon = Icons.Default.Group
                ) {
                    MobileGroupsFilter(
                        groups = state.filterState.groupFilters,
                        onGroupToggle = { group ->
                            onIntent(StartMemberResultsStore.Intent.OnGroupFilterToggle(group.name))
                        }
                    )
                }
            }

            // Пол
            item {
                FilterSection(
                    title = "Пол",
                    icon = Icons.Default.Person
                ) {
                    MobileGendersFilter(
                        genders = state.filterState.genderFilters,
                        onGenderToggle = { gender ->
                            onIntent(StartMemberResultsStore.Intent.OnGenderFilterToggle(gender.name))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterBottomSheetHeader(
    activeFiltersCount: Int,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Заголовок с счетчиком активных фильтров
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Фильтры",
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

            if (activeFiltersCount > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = activeFiltersCount.toString(),
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            }
        }

        // Кнопки действий
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка очистить все
            if (activeFiltersCount > 0) {
                TextButton(
                    onClick = onClearAll,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Очистить",
                        fontFamily = FontNunito.medium(),
                        fontSize = 14.sp
                    )
                }
            }

            // Кнопка закрыть
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    tint = MaterialTheme.colorScheme.tertiary
                )
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
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun MobileFilterChip(
    isSelected: Boolean,
    value: String,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = value,
                fontFamily = FontNunito.medium(),
                fontSize = 14.sp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onTertiary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            )
        },
        leadingIcon = if (isSelected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun MobileDistanceFilter(
    selectedDistance: DistanceFilter?,
    distances: List<DistanceFilter>,
    onDistanceSelected: (DistanceFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(distances) { distance ->
            val isSelected = selectedDistance?.name == distance.name

            MobileFilterChip(
                isSelected = isSelected,
                value = distance.name,
                onClick = { onDistanceSelected(distance) }
            )
        }
    }
}

@Composable
private fun MobileGroupsFilter(
    groups: List<GroupFilter>,
    onGroupToggle: (GroupFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.heightIn(max = 300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups) { group ->
            val isSelected = group.isSelected

            MobileFilterChip(
                isSelected = isSelected,
                value = group.name,
                onClick = { onGroupToggle(group) }
            )
        }
    }
}

@Composable
private fun MobileGendersFilter(
    genders: List<GenderFilter>,
    onGenderToggle: (GenderFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.heightIn(max = 300.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genders) { gender ->
            val isSelected = gender.isSelected

            MobileFilterChip(
                isSelected = isSelected,
                value = gender.name,
                onClick = { onGenderToggle(gender) }
            )
        }
    }
}