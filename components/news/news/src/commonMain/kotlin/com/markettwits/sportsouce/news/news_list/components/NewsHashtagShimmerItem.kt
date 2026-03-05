package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.shimmer

@Composable
fun NewsHashtagShimmerItem(
    modifier: Modifier = Modifier,
) {
    OnBackgroundCard(
        modifier = modifier
            .width(112.dp)
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .shimmer(
                tiltAngle = 30,
                durationMillis = 900,
                gradientColors = listOf(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f),
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f),
                )
            )
    ) {}
}
