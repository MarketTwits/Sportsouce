package com.markettwits.sportsouce.profile.registrations.list.presentation.component

import com.markettwits.sportsouce.profile.registrations.list.presentation.store.RegistrationsStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationsComponent {
    val value: StateFlow<RegistrationsStore.State>
    fun obtainEvent(event: RegistrationsStore.Intent)
}