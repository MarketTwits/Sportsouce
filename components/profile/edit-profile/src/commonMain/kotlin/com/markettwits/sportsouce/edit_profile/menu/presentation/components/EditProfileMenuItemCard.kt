package com.markettwits.sportsouce.edit_profile.menu.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard

@Composable
fun EditProfileMenu(modifier: Modifier = Modifier, menu: List<MenuItem>, onClickItem: (String) -> Unit) {
    OnBackgroundCard(modifier) {
        Column(Modifier.padding(8.dp)) { menu.forEach { item -> EditProfileMenuItemCard(item, onClickItem) } }
    }
}
