package com.markettwits.shop.orders.presentation.components.states

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.impl.SauceComposableErrorRenderImpl

@Composable
internal fun ShopUserOrdersErrorContent(
    modifier: Modifier = Modifier,
    error: SauceError?,
    onClickRetry : () -> Unit,
) {
    if(error != null){
        SauceComposableErrorRenderImpl().ComposableThrowableError(
            modifier = modifier,
            throwable = error,
            onClickRetry = onClickRetry,
            onClickGoBack = null
        )
    }
}