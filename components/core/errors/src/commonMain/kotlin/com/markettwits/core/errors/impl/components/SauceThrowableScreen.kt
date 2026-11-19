package com.markettwits.core.errors.impl.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll

@Composable
internal fun SauceThrowableScreen(
    modifier: Modifier = Modifier,
    sauceError: SauceError,
    onClickRetry: (() -> Unit)? = null,
    onClickGoBack: (() -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val topPadding = if (onClickGoBack != null) {
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 64.dp
        } else {
            24.dp
        }
        SauceThrowableContent(
            modifier = Modifier.align(Alignment.Center)
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .sauceVerticalScroll(rememberScrollState())
                .padding(top = topPadding, bottom = 24.dp),
            sauceError = sauceError,
            onClickRetry = onClickRetry,
        )
        if (onClickGoBack != null) {
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onClickGoBack
            )
        }
    }
}
