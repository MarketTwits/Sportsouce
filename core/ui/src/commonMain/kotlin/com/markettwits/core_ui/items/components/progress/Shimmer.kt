package com.markettwits.core_ui.items.components.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
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
import kotlin.math.cos
import kotlin.math.sin

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
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
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
    val radians = Math.toRadians(tiltAngle.toDouble()).toFloat()

    return this then drawWithContent {
        with(drawContext.canvas) {
            val brush = Brush.linearGradient(
                colors = gradientColors,
                start = Offset(
                    size.width * translateAnimation * cos(radians),
                    size.height * translateAnimation * sin(radians)
                ),
                end = Offset(
                    2 * size.width * translateAnimation * cos(radians),
                    2 * size.height * translateAnimation * sin(radians)
                )
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