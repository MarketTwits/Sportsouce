package com.markettwits.sportsouce.profile.registrations.presentation.list.component

import com.markettwits.sportsouce.profile.registrations.presentation.list.store.RegistrationsStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationsComponent {
    val value: StateFlow<RegistrationsStore.State>
    fun obtainEvent(event: RegistrationsStore.Intent)
}