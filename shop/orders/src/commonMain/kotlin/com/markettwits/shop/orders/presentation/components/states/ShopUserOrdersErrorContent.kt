package com.markettwits.shop.orders.presentation.components.states

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
        ,
        onClickRetry = onClickRetry
    )
}