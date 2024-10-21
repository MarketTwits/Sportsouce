package com.markettwits.shop.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.shop.filter.domain.ShopFilterRepository
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Intent
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Label
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.State

class ShopFilterStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopFilterRepository,
) {
    fun create(): ShopFilterStore = ShopFilterStoreImpl()

    private inner class ShopFilterStoreImpl() :
        ShopFilterStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopFilterStore",
            initialState = State(),
            //bootstrapper = if (state == null) SimpleBootstrapper(Unit) else SimpleBootstrapper(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ShopFilterExecutor(repository) },
            reducer = ShopFilterReducer
        )
}