package com.markettwits.core.errors.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.impl.components.SauceThrowableScreen

@Composable
internal fun SauceThrowableErrorInternal(
    modifier: Modifier,
    throwable: SauceError,
    onClickRetry : () -> Unit,
    onClickGoBack :  (() -> Unit?)?,
) {
    SauceThrowableScreen(
        modifier = modifier,
        throwable = throwable,
        onClickRetry = onClickRetry,
        onClickGoBack = onClickGoBack
    )
}