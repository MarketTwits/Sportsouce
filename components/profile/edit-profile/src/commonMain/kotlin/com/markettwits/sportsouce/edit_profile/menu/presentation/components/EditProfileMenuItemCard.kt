package com.markettwits.sportsouce.edit_profile.menu.presentation.components

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard

@Composable
fun EditProfileMenu(
    modifier: Modifier = Modifier,
    menu: List<MenuItem>,
    onClickItem: (String) -> Unit
) {
    OnBackgroundCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        menu.forEach {
            EditProfileMenuItemCard(item = it) { item ->
                onClickItem(item)
            }
        }
    }
}