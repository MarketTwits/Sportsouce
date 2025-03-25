package com.markettwits.sportsouce.shop.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.shop.filter.domain.ShopFilterRepository
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore.Intent
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore.Label
import com.markettwits.sportsouce.shop.filter.presentation.store.ShopFilterStore.State

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