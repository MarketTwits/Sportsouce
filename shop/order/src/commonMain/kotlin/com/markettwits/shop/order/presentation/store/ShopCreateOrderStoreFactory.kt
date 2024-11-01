package com.markettwits.shop.order.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.domain.ShopOrderRepository
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.State
import com.markettwits.shop.order.presentation.store.ShopCreateOrderStore.Message

class ShopCreateOrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopOrderRepository
) {

    fun create(items : List<ShopItemCart>): ShopCreateOrderStore = ShopCreateOrderStoreImpl(items)

    private inner class ShopCreateOrderStoreImpl(private val items : List<ShopItemCart>) :
        ShopCreateOrderStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopCreateOrderStore",
            initialState = ShopCreateOrderStore.DEFAULT_SHOP_ORDER_STATE.copy(shopOrderItems = items),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ShopCreateOrderExecutor(repository) },
            reducer = ShopCreateOrderReducer
        )
}