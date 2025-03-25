package com.markettwits.sportsouce.settings.internal.change_theme.component


import com.markettwits.sportsouce.settings.internal.change_theme.store.ChangeThemeStore
import kotlinx.coroutines.flow.StateFlow

interface ChangeThemeComponent {
    val state: StateFlow<ChangeThemeStore.State>
    fun obtainEvent(intent: ChangeThemeStore.Intent)
}