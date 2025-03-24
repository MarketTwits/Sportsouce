package com.markettwits.core_ui.items.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
actual fun rememberScreenSizeInfo(): ScreenSizeInfo {
    val density = LocalDensity.current
    val config = LocalWindowInfo.current.containerSize
    return remember(density, config) {
        ScreenSizeInfo(
            hPX = config.height,
            wPX = config.width,
            hDP = with(density) { config.height.toDp() },
            wDP = with(density) { config.width.toDp() }
        )
    }
}