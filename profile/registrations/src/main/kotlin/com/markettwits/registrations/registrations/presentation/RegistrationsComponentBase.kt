package com.markettwits.registrations.registrations.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.registrations.registrations.domain.StartPaymentState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationsComponentBase(
    component: ComponentContext,
    private val storeFactory: RegistrationsDataStoreFactory,
    private val pop: () -> Unit,
    private val showPaymentDialog: (paymentState: StartPaymentState) -> Unit,
    private val onItemClick: (itemId: Int) -> Unit
) : RegistrationsComponent, ComponentContext by component {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    private val scope = CoroutineScope(Dispatchers.Main)
    override val value: StateFlow<RegistrationsStore.State> = store.stateFlow
    override fun obtainEvent(event: RegistrationsStore.Intent) {
        store.accept(event)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is RegistrationsStore.Label.GoBack -> pop()
                    is RegistrationsStore.Label.OnItemClick -> onItemClick(it.itemId)
                    is RegistrationsStore.Label.ShowPaymentDialog -> showPaymentDialog(it.startPaymentState)
                }
            }
        }
    }
}