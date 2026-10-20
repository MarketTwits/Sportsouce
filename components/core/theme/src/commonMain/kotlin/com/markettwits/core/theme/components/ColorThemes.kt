package com.markettwits.core.theme.components

import androidx.compose.material3.ColorScheme
import com.markettwits.core_ui.items.theme.DarkSauceColors
import com.markettwits.core_ui.items.theme.LightSauceColors
import com.markettwits.core_ui.items.theme.SauceColors

internal val LightColorScheme = LightSauceColors.forLegacyScreens()
internal val DarkColorScheme = DarkSauceColors.forLegacyScreens()

/**
 * Existing screens use primary for surfaces, secondary for actions and tertiary for text.
 * Keep that contract at the app boundary until those consumers are migrated. New components
 * use SauceTheme.colors rather than inferring roles from this compatibility scheme.
 * copy preserves all surface, error and fixed roles from the complete semantic palette.
 */
internal fun SauceColors.forLegacyScreens(): ColorScheme = material.copy(
    primary = surface,
    onPrimary = textPrimary,
    primaryContainer = surfaceRaised,
    onPrimaryContainer = textPrimary,
    secondary = action,
    onSecondary = onAction,
    tertiary = textPrimary,
    onTertiary = surface,
    tertiaryContainer = actionSoft,
    onTertiaryContainer = onActionSoft,
    outline = textMuted,
    outlineVariant = surfaceSubtle,
)
