package com.markettwits.shop.orders.presentation.components.states

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core.errors.api.throwable.SauceError

@Composable
internal fun ShopUserOrdersErrorContent(
    modifier: Modifier = Modifier,
    error: SauceError?,
    onClickRetry : () -> Unit,
) {
    error?.SauceErrorScreen(
        modifier = modifier,
        onClickRetry = onClickRetry
    )
}