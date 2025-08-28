package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.gradients

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SportSouceColor
import kotlin.math.PI
import kotlin.math.sin

/**
 * Creates an animated gradient brush for payment-related UI components
 * Uses green and blue color variations for better visual appeal
 */
@Composable
fun rememberAnimatedPaymentGradient(
    colors: List<Color> = listOf(
        SportSouceColor.SportSouceRegistryOpenGreen,
        SportSouceColor.SportSouceLighBlue,
        SportSouceColor.SportSouceBlue
    ),
    direction: GradientDirection = GradientDirection.Horizontal,
    animationDurationMillis: Int = 3000,
): Brush {
    val infiniteTransition = rememberInfiniteTransition()
    val gradientAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Create dynamic alpha variations with balanced visibility
    val animatedColors = colors.mapIndexed { index, color ->
        val phaseOffset = (index * 2 * PI / colors.size).toFloat()
        val alpha = 0.7f + sin(gradientAnimation * 2 * PI + phaseOffset).toFloat() * 0.3f
        color.copy(alpha = alpha)
    }

    return when (direction) {
        GradientDirection.Horizontal -> Brush.horizontalGradient(
            colors = animatedColors,
            startX = gradientAnimation * 300f,
            endX = gradientAnimation * 300f + 300f
        )

        GradientDirection.Vertical -> Brush.verticalGradient(
            colors = animatedColors,
            startY = gradientAnimation * 300f,
            endY = gradientAnimation * 300f + 300f
        )

        GradientDirection.Diagonal -> Brush.linearGradient(
            colors = animatedColors,
            start = androidx.compose.ui.geometry.Offset(
                gradientAnimation * 200f,
                gradientAnimation * 200f
            ),
            end = androidx.compose.ui.geometry.Offset(
                gradientAnimation * 200f + 300f,
                gradientAnimation * 200f + 300f
            )
        )

        GradientDirection.Radial -> Brush.radialGradient(
            colors = animatedColors,
            radius = 200f + gradientAnimation * 100f
        )
    }
}

/**
 * Creates a modifier with animated gradient background for FAB components
 * Uses different green/blue variations than payment button for distinction
 */
@Composable
fun Modifier.animatedFabGradient(
    shape: androidx.compose.foundation.shape.RoundedCornerShape = androidx.compose.foundation.shape.RoundedCornerShape(
        16.dp
    ),
    direction: GradientDirection = GradientDirection.Diagonal,
): Modifier {
    val fabColors = listOf(
        SportSouceColor.SportSouceLightBlueForDarkTheme,
        SportSouceColor.SportSouceRegistryOpenGreen,
        SportSouceColor.SportSouceLighBlue
    )

    val gradientBrush = rememberAnimatedPaymentGradient(
        colors = fabColors,
        direction = direction,
        animationDurationMillis = 4000
    )

    return this.background(
        brush = gradientBrush,
        shape = shape
    )
}

enum class GradientDirection {
    Horizontal,
    Vertical,
    Diagonal,
    Radial
}