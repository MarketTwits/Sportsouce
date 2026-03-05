package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun NewsFeedItemShimmer(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val baseColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
    }
    val shimmerPeak = if (isDarkTheme) {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
    }

    OnBackgroundCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = Shapes.medium
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 220.dp)
                    .background(baseColor)
                    .shimmer(
                        tiltAngle = 30,
                        durationMillis = 900,
                        gradientColors = listOf(
                            baseColor,
                            shimmerPeak,
                            baseColor,
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(baseColor)
                    .shimmer(durationMillis = 900)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(baseColor)
                    .shimmer(durationMillis = 900)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(baseColor)
                    .shimmer(durationMillis = 900)
            )
        }
    }
}
