package com.markettwits.sportsouce.shop.search.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStore.Intent
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStore.Label
import com.markettwits.sportsouce.shop.search.presentation.store.ShopSearchStore.State

class ShopSearchStoreFactory(private val storeFactory: StoreFactory) {

    fun create(query : String): ShopSearchStore = ShopSearchStoreBase(query)

    private inner class ShopSearchStoreBase(private val query: String) :
        ShopSearchStore, Store<Intent, State, Label> by storeFactory.create(
        name = "ShopCatalogStore",
        initialState = State(query = query),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { ShopSearchExecutor() },
        reducer = ShopSearchReducer
    )

}