package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.progress.shimmer

@Composable
fun NewsImageShimmer(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val baseColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.28f)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.62f)
    }
    val highlightColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseColor)
            .shimmer(
                tiltAngle = 28,
                durationMillis = 850,
                gradientColors = listOf(
                    baseColor,
                    highlightColor,
                    baseColor,
                )
            )
    )
}
