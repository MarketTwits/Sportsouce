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

    fun create(state: State?): ShopFilterStore {
        return ShopFilterStoreImpl(state)
    }

    private inner class ShopFilterStoreImpl(
        state: State?,
    ) :
        ShopFilterStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ShopFilterStore",
            initialState = state ?: State(),
            bootstrapper = if (state == null) SimpleBootstrapper(Unit) else SimpleBootstrapper(),
            executorFactory = { ShopFilterExecutor(repository) },
            reducer = ShopFilterReducer
        )
}