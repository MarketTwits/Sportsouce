package com.markettwits.shop.orders.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopUserOrdersComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopUserOrdersStoreFactory,
    private val outputs: ShopUserOrdersComponent.Outputs
) : ShopUserOrdersComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopUserOrdersStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopUserOrdersStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when(it){
                is ShopUserOrdersStore.Label.GoBack -> outputs.goBack()
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }
}
