package com.markettwits.shop.cart.presentation.page.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.page.store.ShopCartPageStoreFactory
import kotlinx.coroutines.flow.StateFlow

class ShopCartPageComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopCartPageStoreFactory,
    private val outputs : ShopCartPageComponent.Outputs,
    private val shopItemCart: ShopItemCart
) : ShopCartPageComponent,
    ComponentContext by componentContext {

    private val store = instanceKeeper.getOrCreate { storeFactory.create(shopItemCart) }

    override val state: StateFlow<ShopCartPageComponent.State>
        get() = store.state

    override fun updateItem(shopItemCart: ShopItemCart) =
        store.updateItem(shopItemCart)

    override fun onClickAddToCart() = store.add()

    override fun onClickRemove() = store.remove()

    override fun onClickCart() = outputs.goToCart()
}
