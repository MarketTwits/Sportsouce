package com.markettwits.sportsouce.settings.internal.settings_menu.component

import com.markettwits.sportsouce.settings.internal.settings_menu.store.SettingsStore
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {
    val state: StateFlow<SettingsStore.State>
    fun obtainEvent(intent: SettingsStore.Intent)
}
