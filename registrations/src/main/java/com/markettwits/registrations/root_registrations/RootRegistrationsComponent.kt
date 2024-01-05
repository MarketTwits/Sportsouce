package com.markettwits.registrations.root_registrations

import com.arkivanov.decompose.value.Value
import com.markettwits.registrations.paymant_dialog.RegistrationsPaymentComponent
import com.markettwits.registrations.registrations.presentation.RegistrationsComponent
import com.markettwits.registrations.registrations.presentation.RegistrationsStore
import kotlinx.serialization.Serializable

interface RootRegistrationsComponent {
    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>

    fun dismissSlotChild()

    @Serializable
    sealed class ConfigStack {
        @Serializable
        data object Registrations : ConfigStack()
    }

    @Serializable
    sealed class ConfigChild {
        @Serializable
        data class PaymentDialog(val paymentState: RegistrationsStore.StartPaymentState) : ConfigChild()
    }


    sealed class ChildStack {
        data class Registrations(val component: RegistrationsComponent) : ChildStack()
    }

    sealed class ChildSlot {
        data class PaymentDialog(val component: RegistrationsPaymentComponent) : ChildSlot()
    }
}