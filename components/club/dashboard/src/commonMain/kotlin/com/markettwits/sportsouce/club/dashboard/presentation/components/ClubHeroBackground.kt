package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.club.cloud.models.subscription.SubscriptionColor
import com.markettwits.sportsouce.club.common.presentation.clubColors

@Composable
internal fun Modifier.clubHeroBackground(phase: () -> Float): Modifier {
    val colors = SauceTheme.colors
    val strength = if (LocalDarkOrLightTheme.current) .20f else .26f
    val blue = SubscriptionColor.BLUE.clubColors().stripeEnd
    val green = SubscriptionColor.GREEN.clubColors().stripeEnd
    return drawWithCache {
        val wash = Brush.verticalGradient(
            0f to lerp(colors.background, colors.actionSoft, .94f),
            .34f to lerp(colors.background, colors.actionSoft, .72f),
            .70f to lerp(colors.background, colors.actionSoft, .36f),
            1f to colors.background,
        )
        val radius = minOf(size.width * .70f, size.height * .42f).coerceAtLeast(1f)
        val blueCenter = Offset(size.width * .12f, size.height * .13f)
        val greenCenter = Offset(size.width * .93f, size.height * .49f)
        val blueGlow = Brush.radialGradient(listOf(blue.copy(alpha = strength), blue.copy(alpha = 0f)), blueCenter, radius)
        val greenGlow = Brush.radialGradient(listOf(green.copy(alpha = strength), green.copy(alpha = 0f)), greenCenter, radius)
        onDrawBehind {
            val progress = phase()
            drawRect(wash)
            clipRect {
                translate(left = size.width * .33f * progress, top = size.height * .09f * progress) {
                    drawCircle(blueGlow, radius, blueCenter)
                }
                translate(left = -size.width * .24f * progress, top = -size.height * .17f * progress) {
                    drawCircle(greenGlow, radius, greenCenter)
                }
            }
        }
    }
}
