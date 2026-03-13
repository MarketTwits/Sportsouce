package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.progress.shimmer

@Composable
fun ResultCardShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .shadow(3.dp, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .shimmer(
                tiltAngle = 30,
                gradientColors = listOf(
                    Color.Transparent,
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                    Color.Transparent,
                ),
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}
