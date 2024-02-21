@file:JvmName("ProfileMenuKt")

package com.markettwits.edit_profile.edit_menu.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.components.OnBackgroundCard

@Composable
fun EditProfileMenu(
    modifier: Modifier = Modifier,
    menu: List<MenuItem>,
    onClickItem: (String) -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        menu.forEach {
            EditProfileMenuItemCard(item = it) {
                onClickItem(it)
            }
        }
    }
}