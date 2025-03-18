package com.markettwits.settings.internal.settings_menu.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard


@Composable
fun SettingsMenuList(
    modifier: Modifier = Modifier,
    menuItems: List<SettingsMenuElement>,
    onClick: (SettingsMenuElement) -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        menuItems.forEach {
            SettingsMenuItem(
                modifier = Modifier.clickable {
                    onClick(it)
                },
                item = it
            )
        }
    }
}