package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.presentation.membres.component.StartMembersScreenComponent
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterGroup
import com.markettwits.sportsouce.start.presentation.membres.models.MembersFilterItem
import com.markettwits.sportsouce.start.presentation.membres.store.StartMembersStore
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun StartMembersFilterDialog(
    component: StartMembersScreenComponent,
) {
    val showDialog by component.showFilterDialog.subscribeAsState()
    val state by component.state.collectAsState()

    if (showDialog) {
        val bottomSheetState = rememberModalBottomSheetState()

        ModalBottomSheet(
            onDismissRequest = { component.closeFilter() },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            dragHandle = {
                Surface(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .width(32.dp)
                        .height(4.dp),
                    shape = RoundedCornerShape(2.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ) {}
            }
        ) {
            MembersFilterBottomSheetContent(
                state = state,
                component = component,
                onDismiss = { component.closeFilter() }
            )
        }
    }
}

@Composable
private fun MembersFilterBottomSheetContent(
    state: StartMembersStore.State,
    component: StartMembersScreenComponent,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Calculate active filters count
    val activeFiltersCount = state.selectedFiltersUi.sumOf { group ->
        if (group.title == "Пол" || group.title == "Дистанция") {
            group.items.count { it is MembersFilterItem.Selected }
        } else 0
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        MembersFilterBottomSheetHeader(
            activeFiltersCount = activeFiltersCount,
            onDismiss = onDismiss,
            onClearAll = {
                val resetFilters = state.selectedFiltersUi.map { group ->
                    if (group.title == "Пол" || group.title == "Дистанция") {
                        group.copy(
                            items = group.items.map { item ->
                                MembersFilterItem.Base(item.title)
                            }
                        )
                    } else group
                }
                component.updateFilter(resetFilters)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Distance filters
            val distanceGroup = state.selectedFiltersUi.find { it.title == "Дистанция" }
            if (distanceGroup != null) {
                item {
                    MembersFilterSection(
                        title = "Дистанция",
                        icon = Icons.Default.LocationOn,
                        filterGroup = distanceGroup,
                        onFilterToggle = { itemTitle ->
                            component.toggleFilter("Дистанция", itemTitle)
                        }
                    )
                }
            }

            // Gender filters
            val genderGroup = state.selectedFiltersUi.find { it.title == "Пол" }
            if (genderGroup != null) {
                item {
                    MembersFilterSection(
                        title = "Пол",
                        icon = Icons.Default.Person,
                        filterGroup = genderGroup,
                        onFilterToggle = { itemTitle ->
                            component.toggleFilter("Пол", itemTitle)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MembersFilterBottomSheetHeader(
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
        // Title with active filters count
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Фильтры",
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
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

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Clear all button
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

            // Close button
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
private fun MembersFilterSection(
    title: String,
    icon: ImageVector,
    filterGroup: MembersFilterGroup,
    onFilterToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Section header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontFamily = FontNunito.semiBoldBold(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Filter chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            filterGroup.items.forEach { item ->
                val isSelected = item is MembersFilterItem.Selected
                MobileFilterChip(
                    isSelected = isSelected,
                    onClick = { onFilterToggle(item.title) },
                    value = item.title
                )
            }
        }
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