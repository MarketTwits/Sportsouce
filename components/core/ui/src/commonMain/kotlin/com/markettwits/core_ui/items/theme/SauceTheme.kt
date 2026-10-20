package com.markettwits.core_ui.items.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalSauceColors = staticCompositionLocalOf { LightSauceColors }

object SauceTheme {
    val colors: SauceColors
        @Composable @ReadOnlyComposable get() = LocalSauceColors.current
}

/** Use directly for previews and new screens; the app root supplies a legacy Material adapter. */
@Composable
fun SauceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    materialColors: ColorScheme = sauceColors(darkTheme).material,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSauceColors provides sauceColors(darkTheme),
        LocalDarkOrLightTheme provides darkTheme,
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = AppTypography(),
            shapes = Shapes,
            content = content,
        )
    }
}
