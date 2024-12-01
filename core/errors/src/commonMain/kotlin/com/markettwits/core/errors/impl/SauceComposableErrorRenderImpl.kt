package com.markettwits.core.errors.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.SauceComposableErrorRender
import com.markettwits.core.errors.api.throwable.SauceError

class SauceComposableErrorRenderImpl : SauceComposableErrorRender {

    @Composable
    override fun ComposableThrowableError(
        modifier: Modifier,
        throwable: SauceError,
        onClickRetry: () -> Unit,
        onClickGoBack :  (() -> Unit?)?
    ) {
        SauceThrowableErrorInternal(
            modifier = modifier,
            throwable = throwable,
            onClickRetry = onClickRetry,
            onClickGoBack = onClickGoBack
        )
    }
}

@Composable
fun rememberSauceErrorRender() : SauceComposableErrorRender = remember {
    SauceComposableErrorRenderImpl()
}
