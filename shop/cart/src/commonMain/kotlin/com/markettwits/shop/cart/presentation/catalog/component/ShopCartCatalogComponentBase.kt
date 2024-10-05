package com.markettwits.shop.cart.presentation.catalog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.shop.cart.presentation.catalog.store.ShopCartCatalogStoreFactory
import kotlinx.coroutines.flow.StateFlow

class ShopCartCatalogComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopCartCatalogStoreFactory,
    private val onClickCartHolder : () -> Unit
) : ShopCartCatalogComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getOrCreate { storeFactory.create() }

    override val state: StateFlow<ShopCartCatalogComponent.State> = store.state

    override fun onClickCart(){
        onClickCartHolder()
    }
}