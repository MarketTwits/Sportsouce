package com.markettwits.core.errors.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError

interface SauceComposableErrorRender {

    @Composable
    fun ComposableThrowableError(
        modifier: Modifier,
        throwable: SauceError,
        onClickRetry : () -> Unit,
        onClickGoBack :  (() -> Unit?)?
    )

}
