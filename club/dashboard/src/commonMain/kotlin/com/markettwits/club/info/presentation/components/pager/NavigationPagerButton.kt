package com.markettwits.club.info.presentation.components.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun NavigationPagerButton(
    modifier: Modifier = Modifier,
    isLeft: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        //Color(0x80000000), // Полупрозрачный черный
                        Color.Transparent
                    )
                )
            )
            .let { if (isLeft) it else it.graphicsLayer { translationX = size.width / 2 } }
    )
}