package com.markettwits.sportsouce.settings.internal.change_theme.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.settings.internal.change_theme.component.ChangeThemeComponent
import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore

@Composable
fun ChangeThemeScreen(component: ChangeThemeComponent) {
    val state by component.state.collectAsState()

    ChangeThemeDialog(onDismissRequest = {
        component.obtainEvent(ChangeThemeStore.Intent.Dismiss)
    }, items = state.items) {
        component.obtainEvent(ChangeThemeStore.Intent.OnClickTheme(it))
    }
}