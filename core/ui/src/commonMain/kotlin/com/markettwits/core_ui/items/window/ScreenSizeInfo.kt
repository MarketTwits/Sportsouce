package com.markettwits.core_ui.items.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ScreenSizeInfo(val hPX: Int, val wPX: Int, val hDP: Dp, val wDP: Dp) {

    fun isPortrait() = wDP <= MAX_PORTRAIT_DP

    private companion object {
        private val MAX_PORTRAIT_DP = 1200.dp
    }
}


@Composable
expect fun rememberScreenSizeInfo(): ScreenSizeInfo