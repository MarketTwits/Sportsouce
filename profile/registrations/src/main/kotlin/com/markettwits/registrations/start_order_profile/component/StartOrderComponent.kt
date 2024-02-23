package com.markettwits.registrations.start_order_profile.component

import com.markettwits.registrations.start_order_profile.store.store.StartOrderStore
import kotlinx.coroutines.flow.StateFlow

interface StartOrderComponent {
    val state: StateFlow<StartOrderStore.State>
    fun obtainEvent(intent: StartOrderStore.Intent)
}
