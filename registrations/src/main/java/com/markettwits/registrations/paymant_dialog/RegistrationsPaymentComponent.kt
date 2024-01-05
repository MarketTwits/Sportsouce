package com.markettwits.registrations.paymant_dialog

import com.arkivanov.decompose.value.Value
import com.markettwits.registrations.registrations.presentation.RegistrationsStore

interface RegistrationsPaymentComponent {
    val state : Value<RegistrationsStore.StartPaymentState>
}