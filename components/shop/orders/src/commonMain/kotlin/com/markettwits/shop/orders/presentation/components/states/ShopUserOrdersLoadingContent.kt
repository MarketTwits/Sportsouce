package com.markettwits.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.LoadingFullScreen

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