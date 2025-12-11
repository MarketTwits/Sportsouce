package com.markettwits.sportsouce.profile.registrations.presentation.detail.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StartOrderComponentBase(
    componentContext: ComponentContext,
    private val start: StartOrderInfo,
    private val storeFactory: StartOrderStoreFactory,
    private val dismiss: () -> Unit,
    private val openStart: (Int) -> Unit,
    private val onReRegistration: (startId: Int, orderId: Int) -> Unit,
) : StartOrderComponent, BottomBarComponentHandler(),
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create(start)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartOrderStore.State> = store.stateFlow

    override fun obtainEvent(intent: StartOrderStore.Intent) {
        store.accept(intent)
    }

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
        scope.launch {
            store.labels.collect {
                when (it) {
                    is StartOrderStore.Label.Dismiss -> dismiss()
                    is StartOrderStore.Label.OnClickStart -> openStart(it.startId)
                    is StartOrderStore.Label.OnClickReRegistration -> onReRegistration(it.startId, it.orderId)
                }
            }
        }
    }
}
