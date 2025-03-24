package com.markettwits.sportsouce.app

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb

@Composable
fun ComponentActivity.SportSauceSystemBarColors(isDarkTheme: Boolean) {
    if (isDarkTheme) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(MaterialTheme.colorScheme.primary.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(MaterialTheme.colorScheme.primary.toArgb()),
        )
    } else {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                MaterialTheme.colorScheme.primary.toArgb(),
                MaterialTheme.colorScheme.primary.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                MaterialTheme.colorScheme.primary.toArgb(),
                MaterialTheme.colorScheme.primary.toArgb()
            ),
        )
    }
}