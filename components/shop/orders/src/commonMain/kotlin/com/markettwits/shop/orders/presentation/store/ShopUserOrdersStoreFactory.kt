package com.markettwits.shop.orders.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Intent
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.shop.orders.domain.ShopUserOrdersRepository
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Label
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.State
import com.markettwits.shop.orders.presentation.store.ShopUserOrdersStore.Message

class ShopUserOrdersStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopUserOrdersRepository,
    private val exceptionTracker: ExceptionTracker
) {

    fun create(): ShopUserOrdersStore {
        return ShopUserOrdersStoreImpl()
    }

    private inner class ShopUserOrdersStoreImpl :
        ShopUserOrdersStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopUserOrdersStore",
            initialState = ShopUserOrdersStore.State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ShopUserOrdersExecutor(repository,exceptionTracker) },
            reducer = ShopUserOrdersReducer
        )
}