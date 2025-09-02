package com.markettwits.sportsouce.app

import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


fun ComponentActivity.sportSauceSystemBarColors(
    isDarkTheme: Boolean,
    colorScheme: ColorScheme,
) {
    // Configure display cutout handling for devices with camera notches
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }

    if (isDarkTheme) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                colorScheme.primary.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                colorScheme.primary.toArgb()
            ),
        )
    } else {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                colorScheme.onBackground.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                colorScheme.primary.toArgb(),
                colorScheme.primary.toArgb()
            ),
        )
    }
}