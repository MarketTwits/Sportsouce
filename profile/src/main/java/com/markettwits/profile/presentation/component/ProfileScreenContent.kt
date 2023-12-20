package com.markettwits.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.profile.presentation.component.menu.MenuItemCard
import com.markettwits.profile.presentation.component.menu.profileMenu

@Composable
fun ProfileScreenContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .background(SportSouceColor.VeryLighBlue)
        .clip(Shapes.medium)
    ) {
        LazyColumn{
            items(profileMenu()){
                MenuItemCard(item = it)
            }
        }
    }
}