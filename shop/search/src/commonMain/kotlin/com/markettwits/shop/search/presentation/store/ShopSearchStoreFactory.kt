package com.markettwits.shop.search.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Intent
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Label
import com.markettwits.shop.search.presentation.store.ShopSearchStore.State
import kotlinx.coroutines.flow.emptyFlow

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