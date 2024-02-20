package com.markettwits.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController



@Composable
fun SportSouceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(
            color = if (darkTheme) Color.Black else Color.White
        )
        systemUiController.setStatusBarColor(
            color = if (darkTheme) Color.Transparent else Color.White
        )
    }
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.White,
    secondaryContainer = SportSouceColor.DirtyWhite,
    onSecondaryContainer = SportSouceColor.VeryLighBlue,
    surfaceTint = SportSouceColor.DirtyWhite,
    tertiary = SportSouceColor.SportSouceBlue,
    tertiaryContainer = Color.Gray,
    background = SportSouceColor.DirtyWhite,
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    onPrimaryContainer = SportSouceColor.SportSouceLighBlue
)

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = SportSouceColor.DarkSecondaryNew,
    secondaryContainer = Color.Black,
    onSecondaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    tertiary = Color.White,
    tertiaryContainer = Color.White,
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    onPrimaryContainer = Color.White,
    surfaceTint = Color.White,
    primaryContainer = Color.DarkGray,
    surface = Color.Black,
    inversePrimary = Color.White,
    onPrimary = SportSouceColor.DirtyWhite,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.White,
    background = Color.Black,
)
