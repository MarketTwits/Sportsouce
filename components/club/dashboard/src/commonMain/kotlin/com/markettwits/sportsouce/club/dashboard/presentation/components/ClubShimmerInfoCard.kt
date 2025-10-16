package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard

@Composable
internal fun ClubShimmerInfoCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    val shimmerAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_animation"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(shimmerAnimation - 300f, shimmerAnimation - 300f),
        end = Offset(shimmerAnimation, shimmerAnimation)
    )

    OnBackgroundCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) { _ ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Title placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description placeholders
                repeat(2) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (index == 1) 0.8f else 1f)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush)
                    )
                    if (index == 0) Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Icon placeholder
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
        }
    }
}