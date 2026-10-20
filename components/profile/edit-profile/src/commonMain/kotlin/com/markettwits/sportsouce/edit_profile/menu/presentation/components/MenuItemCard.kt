package com.markettwits.sportsouce.edit_profile.menu.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun EditProfileMenuItemCard(item: MenuItem, onCLick: (String) -> Unit) {
    when (item) {
        is MenuItem.Base -> SauceActionRow(item.title, { onCLick(item.title) },
            icon = if (item.isDangerZone) Icons.AutoMirrored.Filled.Logout else null)
        is MenuItem.Header -> Text(item.title, Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textSecondary)
    }
}
