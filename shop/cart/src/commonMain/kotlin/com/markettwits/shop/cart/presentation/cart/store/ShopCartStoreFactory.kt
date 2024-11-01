package com.markettwits.shop.cart.presentation.cart.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.shop.cart.domain.ShopCartRepository
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Label
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.State
import com.markettwits.shop.cart.presentation.cart.store.ShopCartStore.Message

class ShopCartStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopCartRepository
) {

    fun create(): ShopCartStore {
        return ShopCartStoreImpl(repository)
    }

    private inner class ShopCartStoreImpl(
        private val repository: ShopCartRepository
    ) :
        ShopCartStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopCartStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ShopCartExecutor(repository) },
            reducer = ShopCartReducer
        )
}