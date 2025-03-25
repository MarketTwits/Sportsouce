package com.markettwits.sportsouce.shop.catalog.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.sportsouce.shop.catalog.domain.ShopCatalogRepository
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.sportsouce.shop.catalog.presentation.store.ShopCatalogStore.State
import kotlinx.coroutines.flow.emptyFlow

class ShopCatalogStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopCatalogRepository,
) {

    fun create(): ShopCatalogStore = ShopCatalogStoreBase(repository)

    private inner class ShopCatalogStoreBase(
        private val repository: ShopCatalogRepository,
    ) :
        ShopCatalogStore, Store<Intent, State, Label> by storeFactory.create(
        name = "ShopCatalogStore",
        initialState = State(
            shopItems = emptyFlow()
        ),
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { ShopCatalogExecutor(repository) },
        reducer = ShopCatalogReducer
    )

}