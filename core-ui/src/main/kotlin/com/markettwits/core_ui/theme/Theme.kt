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
            color = if (darkTheme) SportSouceColor.DarkSecondaryNew else Color.White
        )
        systemUiController.setStatusBarColor(
            color = if (darkTheme) SportSouceColor.DarkSecondaryNew else Color.White
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
    onPrimary = SportSouceColor.DarkSecondaryNew,
    onPrimaryContainer = Color.White,
    secondary = SportSouceColor.SportSouceLighBlue,
    onSecondary = Color.White,
    tertiary = SportSouceColor.SportSouceBlue,
    onTertiary = SportSouceColor.DirtyWhite,
    tertiaryContainer = SportSouceColor.VeryLighBlue,
    onTertiaryContainer = SportSouceColor.SportSouceBlue,
    background = Color.White,
    onBackground = Color.White,
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    outline = Color.Gray
)

private val DarkColorScheme = darkColorScheme(
    primary = SportSouceColor.DarkSecondaryNew,
    onPrimary = SportSouceColor.DirtyWhite,
    onPrimaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    secondary = SportSouceColor.OnSecondaryContainer,
    onSecondary = SportSouceColor.DirtyWhite,
    tertiary = SportSouceColor.DirtyWhite,
    onTertiary = SportSouceColor.DarkSecondaryContainerNew,
    tertiaryContainer = SportSouceColor.DarkSecondaryContainerNew,
    onTertiaryContainer = SportSouceColor.DirtyWhite,
    onErrorContainer = SportSouceColor.SportSouceLightRed,
    outline = Color.LightGray,


    onBackground = Color.White,
    background = Color.Black
)
