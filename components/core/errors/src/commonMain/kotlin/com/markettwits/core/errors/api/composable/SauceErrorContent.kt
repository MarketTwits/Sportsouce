package com.markettwits.core.errors.api.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.impl.components.SauceThrowableContent

@Composable
fun SauceError.SauceErrorContent(
    modifier: Modifier,
    onClickRetry :  (() -> Unit)? = null,
) {
    SauceThrowableContent(
        modifier = modifier,
        sauceError = this,
        onClickRetry = onClickRetry
    )
}