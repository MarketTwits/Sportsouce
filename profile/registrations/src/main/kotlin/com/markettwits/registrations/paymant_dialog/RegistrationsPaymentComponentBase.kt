package com.markettwits.registrations.paymant_dialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStore
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStoreFactory
import com.markettwits.registrations.registrations.domain.StartPaymentState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

class RegistrationsPaymentComponentBase(
    context: ComponentContext,
    private val paymentItems: StartPaymentState,
    private val storeFactory: RegistrationsPaymentStoreFactory
) : RegistrationsPaymentComponent, ComponentContext by context {

    val scope = CoroutineScope(Dispatchers.Main)
    private val store = instanceKeeper.getStore {
        storeFactory.create(paymentItems)
    }
    override val state: StateFlow<RegistrationsPaymentStore.State> = store.stateFlow
    override fun obtainEvent(event: RegistrationsPaymentStore.Intent) {
        store.accept(event)
    }
}