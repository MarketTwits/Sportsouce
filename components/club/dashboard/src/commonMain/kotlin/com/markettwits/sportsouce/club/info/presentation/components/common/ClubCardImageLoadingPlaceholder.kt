package com.markettwits.sportsouce.club.info.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.markettwits.core_ui.items.components.progress.shimmer

@Composable
internal fun ClubCardImageLoadingPlaceholder(
    modifier: Modifier = Modifier,
    shape: Shape,
) {
    Box(
        modifier = modifier
            .shimmer(
                tiltAngle = 30,
                gradientColors = listOf(
                    Color.Transparent,
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                    Color.Transparent
                )
            )
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = shape
            )
    )
}
