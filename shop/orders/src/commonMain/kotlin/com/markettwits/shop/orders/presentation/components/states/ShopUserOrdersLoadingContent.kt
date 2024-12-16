package com.markettwits.shop.orders.presentation.components.states

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen

@Composable
internal fun ShopUserOrdersLoadingContent(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    isListEmpty : Boolean
) {
    if (isLoading && isListEmpty){
        LoadingFullScreen(modifier = modifier
            .fillMaxSize())
    }
}