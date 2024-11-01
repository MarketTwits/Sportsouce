package com.markettwits.shop.catalog.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopCatalogComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopCatalogStoreFactory,
    private val outputs: ShopCatalogComponent.Outputs,
) : ShopCatalogComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopCatalogStore.State>
        get() = store.stateFlow

    override fun obtainEvent(intent: ShopCatalogStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ShopCatalogStore.Label.GoBack -> outputs.goBack()
                is ShopCatalogStore.Label.OnClickItem -> outputs.onClickShopItem(it.item)
                is ShopCatalogStore.Label.GoFilter -> outputs.goFilter()
                is ShopCatalogStore.Label.GoSearch -> outputs.goSearch(it.query)
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))
    }
}