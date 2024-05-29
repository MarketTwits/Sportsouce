package com.markettwits.settings.internal.appearance.component


import com.markettwits.settings.internal.appearance.store.AppearanceStore
import kotlinx.coroutines.flow.StateFlow

interface AppearanceComponent {
    val state: StateFlow<AppearanceStore.State>
    fun obtainEvent(intent: AppearanceStore.Intent)
}