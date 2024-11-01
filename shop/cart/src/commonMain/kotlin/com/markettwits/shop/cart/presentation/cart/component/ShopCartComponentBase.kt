package com.markettwits.shop.cart.presentation.cart.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopCartComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopCartStoreFactory,
    private val outputs : ShopCartComponent.Outputs
) : ShopCartComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ShopCartStore.State> = store.stateFlow

    override fun onClickIncrease(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickIncrease(item))
    }

    override fun onClickDecrease(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickDecrease(item))
    }

    override fun onClickDelete(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickDelete(item))
    }

    override fun onClickShopCartItem(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickItem(item))
    }

    override fun onClickGoBack() {
        outputs.goBack()
    }

    override fun onClickCreateOrder() {
        store.accept(ShopCartStore.Intent.OnClickCreateOrder)
    }

    override fun onClickGoAuth() {
        outputs.goAuth()
    }


    init {
        store.labels.onEach {
            when(it){
                is ShopCartStore.Label.GoToShopItem -> outputs.goShopItem(it.shopItemCart)
                is ShopCartStore.Label.GoOrderScreen -> outputs.goOrder(it.items)
                is ShopCartStore.Label.GoAuth -> outputs.goAuth()
                is ShopCartStore.Label.GoBack -> outputs.goBack()
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))

        lifecycle.doOnResume {
            store.accept(ShopCartStore.Intent.Init)
        }
    }

}