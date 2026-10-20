package com.markettwits.core_ui.items.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/** A foreground/container pair. Always use both roles together. */
@Immutable
data class SauceStatusColors(val content: Color, val container: Color)

/** Semantic palette shared by components, independent of the legacy screen color mapping. */
@Immutable
data class SauceColors(
    val material: ColorScheme,
    val textMuted: Color,
    val controlBorder: Color,
    val disabledContent: Color,
    val disabledContainer: Color,
    val brand: Color,
    val brandContainer: Color,
    val onBrandContainer: Color,
    val success: SauceStatusColors,
    val warning: SauceStatusColors,
    val danger: SauceStatusColors,
    val neutral: SauceStatusColors,
) {
    val action get() = material.primary
    val onAction get() = material.onPrimary
    val actionSoft get() = material.primaryContainer
    val onActionSoft get() = material.onPrimaryContainer
    val background get() = material.background
    val surface get() = material.surface
    val surfaceRaised get() = material.surfaceContainerHigh
    val surfaceSubtle get() = material.surfaceVariant
    val textPrimary get() = material.onSurface
    val textSecondary get() = material.onSurfaceVariant
    val border get() = material.outlineVariant
}

val LightSauceColors: SauceColors = createSauceColors(dark = false)
val DarkSauceColors: SauceColors = createSauceColors(dark = true)

fun sauceColors(dark: Boolean): SauceColors = if (dark) DarkSauceColors else LightSauceColors

private fun createSauceColors(dark: Boolean): SauceColors {
    val background = Color(if (dark) 0xFF111722 else 0xFFFAFBFC)
    val surface = Color(if (dark) 0xFF192231 else 0xFFFFFFFF)
    val raised = Color(if (dark) 0xFF222E40 else 0xFFFFFFFF)
    val subtle = Color(if (dark) 0xFF263142 else 0xFFF0F2F5)
    val text = Color(if (dark) 0xFFEAF0FA else 0xFF1B2438)
    val secondaryText = Color(if (dark) 0xFFB3C0D4 else 0xFF46536A)
    val muted = Color(if (dark) 0xFF9AAAC0 else 0xFF657086)
    val border = Color(if (dark) 0xFF354258 else 0xFFDFE4EB)
    val controlBorder = Color(if (dark) 0xFF73839A else 0xFF8190A8)
    val action = Color(if (dark) 0xFF9DB2FF else 0xFF3159DF)
    val onAction = Color(if (dark) 0xFF15285E else 0xFFFFFFFF)
    val actionSoft = Color(if (dark) 0xFF26365D else 0xFFEEF2FE)
    val brand = Color(if (dark) 0xFFB6C7EA else 0xFF2E3F6E)
    val brandContainer = Color(if (dark) 0xFF263754 else 0xFF2E3F6E)
    val onBrandContainer = Color(if (dark) 0xFFE5EDFC else 0xFFFFFFFF)
    val danger = SauceStatusColors(
        content = Color(if (dark) 0xFFFFAAAA else 0xFFB52E2E),
        container = Color(if (dark) 0xFF482829 else 0xFFFDECEC),
    )
    val base = if (dark) darkColorScheme() else lightColorScheme()
    // Set every role: no default purple surfaces/errors/fixed colors leak into either theme.
    val material = base.copy(
        primary = action,
        onPrimary = onAction,
        primaryContainer = actionSoft,
        onPrimaryContainer = action,
        inversePrimary = Color(if (dark) 0xFF3159DF else 0xFF9DB2FF),
        secondary = action,
        onSecondary = onAction,
        secondaryContainer = actionSoft,
        onSecondaryContainer = action,
        tertiary = brand,
        onTertiary = Color(if (dark) 0xFF1B2A46 else 0xFFFFFFFF),
        tertiaryContainer = Color(if (dark) 0xFF29364F else 0xFFEDF0F8),
        onTertiaryContainer = brand,
        background = background,
        onBackground = text,
        surface = surface,
        onSurface = text,
        surfaceVariant = subtle,
        onSurfaceVariant = secondaryText,
        surfaceTint = Color.Transparent,
        inverseSurface = Color(if (dark) 0xFFEAF0FA else 0xFF1B2438),
        inverseOnSurface = Color(if (dark) 0xFF192231 else 0xFFFAFBFC),
        error = danger.content,
        onError = Color(if (dark) 0xFF482829 else 0xFFFFFFFF),
        errorContainer = danger.container,
        onErrorContainer = danger.content,
        outline = controlBorder,
        outlineVariant = border,
        scrim = Color(0xFF0B1224),
        surfaceBright = raised,
        surfaceContainer = surface,
        surfaceContainerHigh = raised,
        surfaceContainerHighest = subtle,
        surfaceContainerLow = Color(if (dark) 0xFF151D2A else 0xFFF7F8FA),
        surfaceContainerLowest = background,
        surfaceDim = background,
        primaryFixed = Color(0xFFEEF2FE),
        primaryFixedDim = Color(0xFF9DB2FF),
        onPrimaryFixed = Color(0xFF15285E),
        onPrimaryFixedVariant = Color(0xFF2448C5),
        secondaryFixed = Color(0xFFEEF2FE),
        secondaryFixedDim = Color(0xFF9DB2FF),
        onSecondaryFixed = Color(0xFF15285E),
        onSecondaryFixedVariant = Color(0xFF2448C5),
        tertiaryFixed = Color(0xFFE5EDFC),
        tertiaryFixedDim = Color(0xFFB6C7EA),
        onTertiaryFixed = Color(0xFF1B2A46),
        onTertiaryFixedVariant = Color(0xFF2E3F6E),
    )
    return SauceColors(
        material = material,
        textMuted = muted,
        controlBorder = controlBorder,
        disabledContent = muted,
        disabledContainer = Color(if (dark) 0xFF2C3748 else 0xFFE4E8EF),
        brand = brand,
        brandContainer = brandContainer,
        onBrandContainer = onBrandContainer,
        success = SauceStatusColors(
            Color(if (dark) 0xFF77DAB0 else 0xFF116943),
            Color(if (dark) 0xFF163C2E else 0xFFE6F6EE),
        ),
        warning = SauceStatusColors(
            Color(if (dark) 0xFFFFD084 else 0xFF8A5200),
            Color(if (dark) 0xFF453219 else 0xFFFFF4E0),
        ),
        danger = danger,
        neutral = SauceStatusColors(secondaryText, subtle),
    )
}
