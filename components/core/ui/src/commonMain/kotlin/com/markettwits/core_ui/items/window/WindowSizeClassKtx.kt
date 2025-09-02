package com.markettwits.core_ui.items.window

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp


@Composable
@ExperimentalMaterial3WindowSizeClassApi
expect fun calculateWindowSizeClass(): WindowSizeClass

val WindowSizeClass.isLarge: Boolean
    @Composable get() = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium -> false
        // Treat as large only if width is Expanded AND height is reasonably tall (avoid landscape phones)
        WindowWidthSizeClass.Expanded -> screenHeightDp.value >= 600f
        else -> false
    }

val WindowSizeClass.screenWidthPx: Int
    @Composable get() = rememberScreenSizeInfo().wPX

val WindowSizeClass.screenHeightPx: Int
    @Composable get() = rememberScreenSizeInfo().hPX

val WindowSizeClass.screenWidthDp: Dp
    @Composable get() = rememberScreenSizeInfo().wDP

val WindowSizeClass.screenHeightDp: Dp
    @Composable get() = rememberScreenSizeInfo().hDP
