package com.markettwits.sportsouce.profile.registrations.presentation.detail.component

import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent
import kotlinx.coroutines.flow.StateFlow

interface StartOrderComponent {
    val management: RegistrationManagementComponent? get() = null
    val state: StateFlow<StartOrderStore.State>
    fun obtainEvent(intent: StartOrderStore.Intent)
}
