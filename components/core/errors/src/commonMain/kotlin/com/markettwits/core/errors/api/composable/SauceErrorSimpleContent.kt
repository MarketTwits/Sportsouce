package com.markettwits.core.errors.api.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.impl.components.SauceThrowableSimpleContent

@Composable
fun SauceError.SauceErrorSimpleContent(
    modifier: Modifier = Modifier,
    onClickRetry :  (() -> Unit)? = null,
) {
    SauceThrowableSimpleContent(
        modifier = modifier,
        sauceError = this,
        onClickRetry = onClickRetry
    )
}