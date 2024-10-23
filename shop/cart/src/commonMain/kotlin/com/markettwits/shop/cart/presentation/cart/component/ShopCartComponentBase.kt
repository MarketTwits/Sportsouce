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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopCartComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ShopCartStoreFactory,
    private val outputs : ShopCartComponent.Outputs
) : ShopCartComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    override val state: StateFlow<ShopCartStore.State>
        get() = store.stateFlow

    override fun onClickIncrease(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickIncrease(item))
    }

    override fun onClickDecrease(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickDecrease(item))
    }

    override fun onClickShopCartItem(item: ShopItemCart) {
        store.accept(ShopCartStore.Intent.OnClickItem(item))
    }

    override fun onClickGoBack() {
        store.accept(ShopCartStore.Intent.OnClickGoBack)
    }

    override fun onClickChangePaymentType() {
        store.accept(ShopCartStore.Intent.OnClickChangePaymentType)
    }

    override fun onClickCreateOrder() {
        store.accept(ShopCartStore.Intent.OnClickCreateOrder)
    }

    override fun onClickChangeDeliveryWay() {
        store.accept(ShopCartStore.Intent.OnClickChangeDeliveryWay)
    }


    init {
        store.labels.onEach {
            when(it){
                is ShopCartStore.Label.GoBack -> outputs.goBack()
                is ShopCartStore.Label.GoToShopItem -> outputs.goShopItem(it.shopItemCart)
            }
        }.launchIn(CoroutineScope(Dispatchers.Main.immediate))

        lifecycle.doOnResume {
            store.accept(ShopCartStore.Intent.Init)
        }
    }

}