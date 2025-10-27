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
    onPrimary = SportSouceColor.DirtyWhite,
    primaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onPrimaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    secondary = SportSouceColor.SportSouceLightBlueForDarkTheme,
    onSecondary = Color.White,
    tertiary = SportSouceColor.DirtyWhite,
    onTertiary = SportSouceColor.DarkSecondaryContainerNew,
    tertiaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onTertiaryContainer = SportSouceColor.DirtyWhite,
    error = Color(255, 140, 140),
    onError = Color.Black,
    errorContainer = Color(255, 140, 140).copy(alpha = 0.15f),
    onErrorContainer = Color(255, 140, 140),
    outline = SportSouceColor.GrayForDarkTheme,
    surfaceTint = Color.White.copy(alpha = 0.05f),
    outlineVariant = SportSouceColor.DarkSecondaryContainerNew,
    onBackground = Color.White,
    background = Color.Black
)