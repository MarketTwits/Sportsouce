package com.markettwits.profile.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.presentation.common.menu.MenuItem
import com.markettwits.profile.presentation.common.menu.MenuItemCard
import com.markettwits.profile.presentation.common.menu.profileMenu

@Composable
fun ProfileScreenContent(modifier: Modifier = Modifier, menu : List<MenuItem>, onClickItem : (String) -> Unit) {
    Column(modifier = modifier
        .background(SportSouceColor.VeryLighBlue)
        .clip(Shapes.medium)
    ) {
        LazyColumn{
            items(menu){
                MenuItemCard(item = it){
                    onClickItem(it)
                }
            }
        }
    }
}