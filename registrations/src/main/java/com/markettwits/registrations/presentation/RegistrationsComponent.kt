package com.markettwits.registrations.presentation

import kotlinx.coroutines.flow.StateFlow

interface RegistrationsComponent {
    val value : StateFlow<RegistrationsStore.State>
    fun obtainEvent(event : RegistrationsStore.Intent)
}