package com.markettwits.registrations.detail.component

import com.markettwits.registrations.detail.store.store.StartOrderStore
import kotlinx.coroutines.flow.StateFlow

interface StartOrderComponent {
    val state: StateFlow<StartOrderStore.State>
    fun obtainEvent(intent: StartOrderStore.Intent)
}
