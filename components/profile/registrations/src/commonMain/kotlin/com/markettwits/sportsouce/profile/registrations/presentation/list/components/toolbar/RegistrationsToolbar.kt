package com.markettwits.sportsouce.profile.registrations.presentation.list.components.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.presentation.list.component.RegistrationsComponent
import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore

@Composable
internal fun RegistrationsToolbar(
    component: RegistrationsComponent,
    activeFilters: Int,
    showFilters: Boolean,
) {
    TopBarBase(title = "Мои регистрации", titleMaxLines = 1, endAction = if (showFilters) {
        { RegistrationFiltersButton(activeFilters, component::openFilter) }
    } else null) {
        component.obtainEvent(RegistrationsStore.Intent.Pop)
    }
}

@Composable
private fun RegistrationFiltersButton(activeFilters: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        BadgedBox(badge = { if (activeFilters > 0) Badge { Text(activeFilters.toString()) } }) {
            Icon(Icons.Default.FilterList, "Фильтры", tint = SauceTheme.colors.textPrimary)
        }
    }
}
