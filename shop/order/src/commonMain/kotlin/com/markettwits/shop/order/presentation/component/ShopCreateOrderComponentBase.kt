package com.markettwits.shop.order.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class ShopCreateOrderComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopCreateOrderStoreFactory,
    private val outputs: ShopCreateOrderComponent.Outputs,
    private val option : Option
) : ShopCreateOrderComponent, ComponentContext by componentContext{

    private val store = instanceKeeper.getStore {
        storeFactory.create(option.shopOrderItems)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopCreateOrderStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopCreateOrderStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when(it){
                ShopCreateOrderStore.Label.GoBack -> outputs.goBack()
            }
        }.launchIn(componentScope)
    }

    @Serializable
    data class Option(val shopOrderItems : List<ShopItemCart>)
}