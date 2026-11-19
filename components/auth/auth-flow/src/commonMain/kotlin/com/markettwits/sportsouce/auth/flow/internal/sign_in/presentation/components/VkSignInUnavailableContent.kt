package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun VkSignInUnavailableContent(modifier: Modifier = Modifier) {
    val colors = SauceTheme.colors
    Column(
        modifier = modifier.fillMaxWidth()
            .clip(SauceShapes.card)
            .background(colors.surface)
            .drawWithCache {
                val outline = SauceShapes.card.createOutline(size, layoutDirection, this)
                val stroke = Stroke(width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx())))
                onDrawBehind { drawOutline(outline, colors.border, style = stroke) }
            }
            .padding(SauceSpacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.large),
    ) {
        Box(
            Modifier.size(48.dp).clip(SauceShapes.button).background(colors.surfaceSubtle),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(24.dp),
                tint = colors.textSecondary)
        }
        Text(
            text = "Вход через ВКонтакте сейчас недоступен",
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Войдите по телефону или почте и паролю — это займёт минуту.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textMuted,
            textAlign = TextAlign.Center,
        )
    }
}
