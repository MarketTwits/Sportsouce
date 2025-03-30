package com.markettwits.sportsouce.profile.registrations.detail.component

import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStore
import kotlinx.coroutines.flow.StateFlow

interface StartOrderComponent {
    val state: StateFlow<StartOrderStore.State>
    fun obtainEvent(intent: StartOrderStore.Intent)
}
