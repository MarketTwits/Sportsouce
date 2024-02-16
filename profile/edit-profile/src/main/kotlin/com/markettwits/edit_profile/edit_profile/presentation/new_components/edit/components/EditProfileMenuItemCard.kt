@file:JvmName("ProfileMenuKt")

package com.markettwits.edit_profile.edit_profile.presentation.new_components.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.markettwits.core_ui.components.Shapes
import com.markettwits.edit_profile.edit_profile.presentation.new_components.edit.MenuItem

@Composable
fun EditProfileMenu(
    modifier: Modifier = Modifier,
    menu: List<MenuItem>,
    onClickItem: (String) -> Unit
) {
    Column(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        LazyColumn(userScrollEnabled = false) {
            items(menu) {
                EditProfileMenuItemCard(item = it) {
                    onClickItem(it)
                }
            }
        }
    }
}