package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton

@Composable
internal fun SauceThrowableScreen(
    modifier: Modifier = Modifier,
    sauceError : SauceError,
    onClickRetry :  (() -> Unit)? = null,
    onClickGoBack : (() -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (onClickGoBack != null) {
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onClickGoBack
            )
        }
        SauceThrowableContent(
            modifier = Modifier
                .align(Alignment.Center),
            sauceError = sauceError,
            onClickRetry = onClickRetry
        )
    }
}