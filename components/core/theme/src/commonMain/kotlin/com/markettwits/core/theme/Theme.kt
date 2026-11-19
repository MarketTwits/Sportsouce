package com.markettwits.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import coil3.compose.setSingletonImageLoaderFactory
import com.markettwits.core.theme.component.ThemeComponent
import com.markettwits.core.theme.components.isDark
import com.markettwits.core.theme.components.systemColorPallet
import com.markettwits.core_ui.items.image.asyncImageLoader
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun SportSauceTheme(
    component: ThemeComponent,
    content: @Composable (Boolean) -> Unit
) {
    val theme = component.getAppTheme().collectAsState()
    val isDark = isDark(theme = theme.value)
    val palette = systemColorPallet(theme = theme.value, systemIsDark = isSystemInDarkTheme())

    SportSauceTheme(
        isDark = isDark,
        colorScheme = palette,
        content = { content(isDark) }
    )
}

@Composable
private fun SportSauceTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    setSingletonImageLoaderFactory { context ->
        context.asyncImageLoader()
    }

    SauceTheme(darkTheme = isDark, materialColors = colorScheme, content = content)
}

