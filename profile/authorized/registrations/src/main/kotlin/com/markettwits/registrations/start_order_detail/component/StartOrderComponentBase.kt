package com.markettwits.registrations.start_order_detail.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStore
import com.markettwits.registrations.start_order_detail.store.store.StartOrderStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartOrderComponentBase(
    componentContext: ComponentContext,
    private val start: StartOrderInfo,
    private val storeFactory: StartOrderStoreFactory,
    private val dismiss: () -> Unit,
    private val openStart: (Int) -> Unit
) : StartOrderComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create(start)
    }
    override val state: StateFlow<StartOrderStore.State> = store.stateFlow
    override fun obtainEvent(intent: StartOrderStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartOrderStore.Label.Dismiss -> dismiss()
                    is StartOrderStore.Label.OnClickStart -> {
                        dismiss()
                        openStart(it.startId)
                    }
                }
            }
        }
    }
}
