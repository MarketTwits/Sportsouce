package com.markettwits.shop.item.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.item.presentation.store.ShopItemPageStore
import com.markettwits.shop.item.presentation.store.ShopItemPageStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable

class ShopItemPageComponentBase(
    componentContext: ComponentContext,
    storeFactory: ShopItemPageStoreFactory,
    options: Options,
    outputs: ShopItemPageComponent.Output,
) : ShopItemPageComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(productId = options.productId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopItemPageStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopItemPageStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ShopItemPageStore.Label.GoBack -> outputs.goBack()
                is ShopItemPageStore.Label.UpdateItem -> outputs.updateItem(it.shopPageItem)
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }

    @Serializable
    data class Options(val productId: String)

}