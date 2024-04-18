package com.markettwits.theme.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme
import com.markettwits.core_ui.items.theme.Typography
import com.markettwits.theme.theme.component.ThemeComponent
import com.markettwits.theme.theme.components.isDark
import com.markettwits.theme.theme.components.systemColorPallet

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
    MaterialTheme(
        shapes = Shapes(medium = RoundedCornerShape(size = 10.dp)),
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        CompositionLocalProvider(LocalDarkOrLightTheme provides isDark, content = content)
    }
}

