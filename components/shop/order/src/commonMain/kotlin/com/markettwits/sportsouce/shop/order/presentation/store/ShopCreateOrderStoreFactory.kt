package com.markettwits.sportsouce.shop.order.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.sportsouce.shop.cart.domain.ShopCartRepository
import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.order.domain.ShopOrderRepository
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.Intent
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.Label
import com.markettwits.sportsouce.shop.order.presentation.store.ShopCreateOrderStore.State

class ShopCreateOrderStoreFactory(
    private val storeFactory: StoreFactory,
    private val orderRepository: ShopOrderRepository,
    private val cartRepository: ShopCartRepository,
    private val intentAction: IntentAction
) {

    fun create(items : List<ShopItemCart>): ShopCreateOrderStore = ShopCreateOrderStoreImpl(items)

    private inner class ShopCreateOrderStoreImpl(
        private val items : List<ShopItemCart>,
    ) :
        ShopCreateOrderStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopCreateOrderStore",
            initialState = ShopCreateOrderStore.DEFAULT_SHOP_ORDER_STATE.copy(shopOrderItems = items),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ShopCreateOrderExecutor(
                orderRepository,
                cartRepository,
                intentAction
            ) },
            reducer = ShopCreateOrderReducer
        )
}