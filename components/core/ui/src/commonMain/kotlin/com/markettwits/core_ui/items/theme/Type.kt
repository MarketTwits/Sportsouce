package com.markettwits.core_ui.items.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.unit.sp

@Composable
fun AppTypography(): Typography {
    val family = FontRoboto.family()
    fun style(size: Int, lineHeight: Int, weight: FontWeight = FontWeight.Normal, tracking: Float = 0f) = TextStyle(
        fontFamily = family,
        fontWeight = weight,
        fontSize = size.sp,
        lineHeight = lineHeight.sp,
        letterSpacing = tracking.sp,
        fontSynthesis = FontSynthesis.Style,
    )
    return Typography(
        displayLarge = style(57, 64, FontWeight.SemiBold),
        displayMedium = style(45, 52, FontWeight.SemiBold),
        displaySmall = style(36, 44, FontWeight.SemiBold),
        headlineLarge = style(32, 40, FontWeight.SemiBold),
        headlineMedium = style(28, 36, FontWeight.SemiBold),
        headlineSmall = style(24, 32, FontWeight.SemiBold),
        titleLarge = style(22, 30, FontWeight.Medium),
        titleMedium = style(18, 26, FontWeight.Medium),
        titleSmall = style(16, 24, FontWeight.Medium, 0.1f),
        bodyLarge = style(16, 24, tracking = 0.15f),
        bodyMedium = style(14, 21, tracking = 0.1f),
        bodySmall = style(12, 18, tracking = 0.1f),
        labelLarge = style(14, 20, FontWeight.Medium, 0.1f),
        labelMedium = style(12, 16, FontWeight.Medium, 0.1f),
        labelSmall = style(11, 16, tracking = 0.1f),
    )
}
