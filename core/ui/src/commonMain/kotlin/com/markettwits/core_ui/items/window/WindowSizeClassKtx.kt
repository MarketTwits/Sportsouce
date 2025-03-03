package com.markettwits.core_ui.items.window

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

val WindowSizeClass.isLarge: Boolean
    @Composable get() = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Expanded -> true
        WindowWidthSizeClass.Medium -> true
        else -> true
    }

val WindowSizeClass.screenWidthPx: Int
    @Composable get() = rememberScreenSizeInfo().wPX

val WindowSizeClass.screenHeightPx: Int
    @Composable get() = rememberScreenSizeInfo().hPX

val WindowSizeClass.screenWidthDp: Dp
    @Composable get() = rememberScreenSizeInfo().wDP

val WindowSizeClass.screenHeightDp: Dp
    @Composable get() = rememberScreenSizeInfo().hDP
