package com.markettwits.theme.theme.components

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.theme.SportSouceColor

internal val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = SportSouceColor.DarkSecondaryNew,
    onPrimaryContainer = Color.White,
    secondary = SportSouceColor.SportSouceLighBlue,
    onSecondary = Color.White,
    tertiary = SportSouceColor.SportSouceBlue,
    onTertiary = SportSouceColor.DirtyWhite,
    tertiaryContainer = SportSouceColor.VeryLighBlue,
    onTertiaryContainer = SportSouceColor.SportSouceBlue,
    background = Color.White,
    onBackground = Color.Black,
    primaryContainer = Color.White,
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    outline = Color.Gray,
    outlineVariant = SportSouceColor.SecondaryWhite,
)
internal val DarkColorScheme = darkColorScheme(
    primary = SportSouceColor.DarkSecondaryNew,
    onPrimary = SportSouceColor.DirtyWhite,
    primaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onPrimaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    secondary = SportSouceColor.OnSecondaryContainer,
    onSecondary = SportSouceColor.DirtyWhite,
    tertiary = SportSouceColor.DirtyWhite,
    onTertiary = SportSouceColor.DarkSecondaryContainerNew,
    tertiaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onTertiaryContainer = SportSouceColor.DirtyWhite,
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    outline = Color.Gray,
    outlineVariant = SportSouceColor.DarkSecondaryContainerNew,
    onBackground = Color.White,
    background = Color.Black
)