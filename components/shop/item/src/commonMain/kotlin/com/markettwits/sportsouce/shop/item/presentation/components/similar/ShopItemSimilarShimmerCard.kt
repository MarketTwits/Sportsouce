package com.markettwits.sportsouce.shop.item.presentation.components.similar

import com.markettwits.core_ui.items.theme.SauceTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.Shapes

@Composable
internal fun ShopItemSimilarShimmerCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shimmer(
                tiltAngle = 30,
                gradientColors = listOf(
                    Color.Transparent,
                    SauceTheme.colors.textPrimary.copy(alpha = 0.1f),
                    Color.Transparent,
                )
            )
            .padding(4.dp)
            .width(120.dp)
            .height(260.dp)
            .background(
                color = SauceTheme.colors.surface,
                shape = Shapes.large
            )

    )
}
