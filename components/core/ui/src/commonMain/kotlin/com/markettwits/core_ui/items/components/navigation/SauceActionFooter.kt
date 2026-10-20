package com.markettwits.core_ui.items.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.theme.SauceTheme

/** For Scaffold bottomBar slots. Insets already consumed by a parent are not applied twice. */
@Composable
fun SauceActionFooter(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth(), color = SauceTheme.colors.surface, contentColor = SauceTheme.colors.textPrimary) {
        Column(Modifier.windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime)), content = content)
    }
}
