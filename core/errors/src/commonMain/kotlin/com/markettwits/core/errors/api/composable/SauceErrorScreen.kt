package com.markettwits.core.errors.api.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.impl.components.SauceThrowableScreen

@Composable
fun SauceError.SauceErrorScreen(
    modifier: Modifier = Modifier,
    onClickRetry :  (() -> Unit)? = null,
    onClickGoBack : (() -> Unit)? = null,
) {
    SauceThrowableScreen(
        modifier = modifier,
        sauceError = this,
        onClickRetry = onClickRetry,
        onClickGoBack = onClickGoBack
    )
}