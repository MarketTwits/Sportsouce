package com.markettwits.shop.item.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.item.presentation.store.ShopItemPageStore
import com.markettwits.shop.item.presentation.store.ShopItemPageStoreFactory
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class ShopItemPageComponentBase(
    componentContext: ComponentContext,
    storeFactory: ShopItemPageStoreFactory,
    options: Options,
) : ShopItemPageComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(options.productId)
    }

    override val state: StateFlow<ShopItemPageStore.State> = store.stateFlow

    override fun obtainEvent(intent: ShopItemPageStore.Intent) {
        store.accept(intent)
    }

    @Serializable
    data class Options(val productId: String)

}