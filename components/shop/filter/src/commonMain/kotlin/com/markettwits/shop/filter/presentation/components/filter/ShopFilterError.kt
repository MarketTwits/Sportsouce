package com.markettwits.shop.filter.presentation.components.filter

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.screens.FailedScreen

@Composable
internal fun ShopFilterError(
    modifier: Modifier = Modifier,
    isError : Boolean,
    message : String,
    onClickRetry : () -> Unit
) {
    if (isError){
        FailedScreen(
            modifier = modifier,
            message = message,
            onClickRetry = onClickRetry
        )
    }
}