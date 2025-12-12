package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptivePane(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = calculateWindowSizeClass(),
    backgroundColor: Color = Color.Transparent,
    content: @Composable (BoxScope) -> Unit
) {
    val screenWidth = rememberScreenSizeInfo().wDP
    val maxWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> Dp.Unspecified
        WindowWidthSizeClass.Medium -> (screenWidth * 0.8f).coerceAtMost(800.dp)
        WindowWidthSizeClass.Expanded -> (screenWidth * 0.7f).coerceAtMost(1350.dp)
        else -> Dp.Unspecified
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .widthIn(max = maxWidth)
        ) {
            content(this)
        }
    }
}

enum class AdaptivePaneMode {
    COLUMN, BOX
}