package com.markettwits.sportsouce.profile.registrations.presentation.list.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.bottom_sheet.SauceModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationsFilterDialog(component: RegistrationsComponent) {
    val showDialog by component.showFilterDialog.subscribeAsState()
    if (!showDialog) return

    val state by component.value.collectAsState()
    val colors = SauceTheme.colors
    val selected = state.filter.filter { it.checked }

    SauceModalBottomSheet(
        onDismissRequest = component::closeFilter,
        shape = SauceShapes.sheet,
        containerColor = colors.surface,
        contentColor = colors.textPrimary,
    ) {
        SauceLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = SauceSpacing.large,
                end = SauceSpacing.large,
                bottom = SauceSpacing.large,
            ),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Фильтры",
                        modifier = Modifier.weight(1f).semantics { heading() },
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (selected.isNotEmpty()) {
                        SauceStatusBadge(selected.size.toString(), showIndicator = false)
                    }
                    IconButton(onClick = component::closeFilter, modifier = Modifier.size(SauceSizes.touchTarget)) {
                        Icon(Icons.Default.Close, contentDescription = "Закрыть", tint = colors.textSecondary)
                    }
                }
            }
            if (state.filter.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Outlined.Payment, null, Modifier.size(SauceSizes.icon), tint = colors.textSecondary)
                            Text("Статус оплаты", style = MaterialTheme.typography.titleSmall)
                        }
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                            verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall),
                        ) {
                            state.filter.forEach { filter ->
                                FilterChipBase(
                                    selected = filter.checked,
                                    label = filter.value,
                                    onClick = { component.toggleFilter(filter) },
                                )
                            }
                        }
                    }
                }
            }
            if (selected.isNotEmpty()) {
                item {
                    SauceButton(
                        title = "Сбросить фильтры",
                        onClick = { selected.forEach(component::toggleFilter) },
                        variant = SauceButtonVariant.Text,
                    )
                }
            }
        }
    }
}
