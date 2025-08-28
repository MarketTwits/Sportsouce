package com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationsFilterDialog(
    component: RegistrationsComponent,
) {
    val showDialog by component.showFilterDialog.subscribeAsState()
    val state by component.value.collectAsState()

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
            RegistrationsFilterBottomSheetContent(
                filters = state.filter,
                onFilterToggle = { filterItem ->
                    component.toggleFilter(filterItem)
                },
                onDismiss = { component.closeFilter() },
                onClearAll = {
                    // Clear all filters by toggling all checked filters
                    state.filter.filter { it.checked }.forEach { filterItem ->
                        component.toggleFilter(filterItem)
                    }
                }
            )
        }
    }
}

@Composable
private fun RegistrationsFilterBottomSheetContent(
    filters: List<FilterItem>,
    onFilterToggle: (FilterItem) -> Unit,
    onDismiss: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val activeFiltersCount = filters.count { it.checked }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        RegistrationsFilterBottomSheetHeader(
            activeFiltersCount = activeFiltersCount,
            onDismiss = onDismiss,
            onClearAll = onClearAll
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (filters.isNotEmpty()) {
                item {
                    RegistrationsFilterSection(
                        title = "Статус оплаты",
                        icon = Icons.Default.Payment,
                        filters = filters,
                        onFilterToggle = onFilterToggle
                    )
                }
            }
        }
    }
}

@Composable
private fun RegistrationsFilterBottomSheetHeader(
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
private fun RegistrationsFilterSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    filters: List<FilterItem>,
    onFilterToggle: (FilterItem) -> Unit,
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
            filters.forEach { filterItem ->
                MobileFilterChip(
                    isSelected = filterItem.checked,
                    onClick = { onFilterToggle(filterItem) },
                    value = filterItem.value
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