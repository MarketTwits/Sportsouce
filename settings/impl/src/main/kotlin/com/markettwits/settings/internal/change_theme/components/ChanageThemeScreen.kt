package com.markettwits.settings.internal.change_theme.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.settings.internal.change_theme.store.ChangeThemeStore

@Composable
fun ChangeThemeScreen(component: com.markettwits.settings.internal.change_theme.component.ChangeThemeComponent) {
    val state by component.state.collectAsState()

    ChangeThemeDialog(onDismissRequest = {
        component.obtainEvent(ChangeThemeStore.Intent.Dismiss)
    }, items = state.items) {
        component.obtainEvent(ChangeThemeStore.Intent.OnClickTheme(it))
    }
}