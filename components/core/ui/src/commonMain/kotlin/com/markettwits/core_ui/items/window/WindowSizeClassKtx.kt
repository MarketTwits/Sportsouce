package com.markettwits.core_ui.items.window

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.toSize


@Composable
@ExperimentalMaterial3WindowSizeClassApi
expect fun calculateWindowSizeClass(): WindowSizeClass

val WindowSizeClass.isLarge: Boolean
    @Composable get() = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium -> false
        WindowWidthSizeClass.Expanded -> true
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
