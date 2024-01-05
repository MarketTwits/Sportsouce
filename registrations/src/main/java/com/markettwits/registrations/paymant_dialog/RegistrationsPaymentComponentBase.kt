package com.markettwits.registrations.paymant_dialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.markettwits.registrations.registrations.presentation.RegistrationsStore

class RegistrationsPaymentComponentBase(private val paymentItems: RegistrationsStore.StartPaymentState) :
    RegistrationsPaymentComponent {
    override val state: Value<RegistrationsStore.StartPaymentState> = MutableValue(paymentItems)
}