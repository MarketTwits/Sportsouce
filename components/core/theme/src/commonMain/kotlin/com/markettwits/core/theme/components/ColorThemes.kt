package com.markettwits.core.theme.components

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.items.theme.SportSouceColor

internal val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = SportSouceColor.OnBackgroundLight,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.White,
    secondary = SportSouceColor.SportSouceLighBlue,
    onSecondary = Color.White,
    tertiary = SportSouceColor.SportSouceBlue,
    onTertiary = SportSouceColor.DirtyWhite,
    tertiaryContainer = SportSouceColor.VeryLighBlue,
    onTertiaryContainer = SportSouceColor.SportSouceBlue,
    background = Color.White,
    onBackground = SportSouceColor.OnBackgroundLight,
    surface = Color.White,
    onSurface = SportSouceColor.OnBackgroundLight,
    surfaceVariant = SportSouceColor.SecondaryWhite,
    onSurfaceVariant = SportSouceColor.OnBackgroundLight,
    error = SportSouceColor.SportSouceLightRed,
    onError = Color.White,
    errorContainer = SportSouceColor.SportSouceLightRed.copy(alpha = 0.1f),
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    outline = SportSouceColor.Gray,
    surfaceTint = Color.Black.copy(alpha = 0.05f),
    outlineVariant = SportSouceColor.SecondaryWhite,
)
internal val DarkColorScheme = darkColorScheme(
    primary = SportSouceColor.DarkSecondaryNew,
    onPrimary = SportSouceColor.OnPrimaryDark,
    primaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onPrimaryContainer = SportSouceColor.OnPrimaryDark,
    secondary = SportSouceColor.SportSouceLightBlueForDarkTheme,
    onSecondary = Color.White,
    tertiary = SportSouceColor.DarkTertiary,
    onTertiary = SportSouceColor.DarkSecondaryContainerNew,
    tertiaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onTertiaryContainer = SportSouceColor.OnPrimaryDark,
    error = SportSouceColor.SportSouceDarkRed,
    onError = Color.Black,
    errorContainer = SportSouceColor.SportSouceDarkRed.copy(alpha = 0.15f),
    onErrorContainer = SportSouceColor.SportSouceDarkRed,
    outline = SportSouceColor.GrayForDarkTheme,
    surfaceTint = Color.White.copy(alpha = 0.05f),
    outlineVariant = SportSouceColor.DarkSecondaryContainerNew,
    onBackground = SportSouceColor.OnPrimaryDark,
    background = Color.Black
)