package com.markettwits.registrations.paymant_dialog

import com.arkivanov.decompose.value.Value
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
import kotlinx.coroutines.flow.StateFlow

interface RegistrationsPaymentComponent {
    val state : StateFlow<RegistrationsPaymentStore.State>
    fun obtainEvent(event : RegistrationsPaymentStore.Intent)
}