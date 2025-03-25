package com.markettwits.sportsouce.shop.catalog.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun ShopItemShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shimmer(
                tiltAngle = 30,
                gradientColors = listOf(
                    Color.Transparent,
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                    Color.Transparent,
                )
            )
            .padding(4.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = Shapes.large
            )

    )
}