package com.markettwits.core_ui.items.components.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.withSaveLayer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.abs

/**
 * Custom modifier for flickering animation
 *
 * @param gradientColors list of colors for the shimmer effect
 * @param showShimmer whether to show shimmer
 * @param tiltAngle angle of the flicker relative to the vertical axis from 0 to 360
 * @param periodMillis duration of one animation procedure in milliseconds
 */
@Composable
fun Modifier.shimmer(
    gradientColors: List<Color> = listOf(
        Color.Transparent,
        SauceTheme.colors.textMuted.copy(alpha = 0.15f),
        Color.Transparent,
    ),
    showShimmer: Boolean = true,
    tiltAngle: Int = 0,
    durationMillis: Int = 1000,
): Modifier {
    if (!showShimmer) return this

    val transition = rememberInfiniteTransition(label = "shimmer animation transition")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer translate animation"
    )
    val radians = (tiltAngle * PI / 180).toFloat()
    return drawWithContent {
        with(drawContext.canvas) {
            val axis = Offset(cos(radians), sin(radians))
            val extent = (size.width * abs(axis.x) + size.height * abs(axis.y)) / 2f
            val band = maxOf(extent * 0.7f, 1f)
            val position = -extent - band + (2f * extent + 2f * band) * translateAnimation
            val center = Offset(size.width / 2f, size.height / 2f) + axis * position
            val brush = Brush.linearGradient(
                colors = gradientColors,
                start = center - axis * band,
                end = center + axis * band,
            )
            withSaveLayer(
                bounds = size.toRect(),
                paint = Paint()
            ) {
                drawContent()
                drawRect(
                    brush = brush,
                    blendMode = BlendMode.SrcAtop
                )
            }
        }
    }
}
