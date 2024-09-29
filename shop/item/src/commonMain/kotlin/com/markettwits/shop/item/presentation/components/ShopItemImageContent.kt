package com.markettwits.shop.item.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.FullImageContent

@Composable
internal fun ShopItemImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    var isFullScreen: Boolean by remember {
        mutableStateOf(false)
    }
    FullImageContent(
        modifier = modifier.clickable {
            isFullScreen = !isFullScreen
        },
        imageUrl = imageUrl
    )
    AnimatedVisibility(
        visible = isFullScreen,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        FullImageScreen(image = imageUrl, onDismiss = { isFullScreen = !isFullScreen })
    }
}