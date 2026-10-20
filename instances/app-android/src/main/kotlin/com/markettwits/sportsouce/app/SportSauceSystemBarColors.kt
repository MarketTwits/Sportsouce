package com.markettwits.sportsouce.app

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.toArgb


fun ComponentActivity.sportSauceSystemBarColors(
    isDarkTheme: Boolean,
    colorScheme: ColorScheme,
) {
    enableEdgeToEdge(
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = android.graphics.Color.TRANSPARENT,
            darkScrim = android.graphics.Color.TRANSPARENT,
            detectDarkMode = { isDarkTheme }
        ),
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = colorScheme.background.toArgb(),
            darkScrim = colorScheme.background.toArgb(),
            detectDarkMode = { isDarkTheme }
        ),
    )
}