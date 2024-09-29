package com.markettwits.shop.item.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.IntentAction
import com.markettwits.shop.item.domain.ShopItemRepository
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Intent
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Label
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.State

class ShopItemPageStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ShopItemRepository,
    private val intentAction: IntentAction,
) {

    fun create(productId: String): ShopItemPageStore =
        ShopItemPageStoreImpl(productId)

    private inner class ShopItemPageStoreImpl(private val id: String) :
        ShopItemPageStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ShopItemPageStore",
            initialState = State(false, false, "", null),
            bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = { ShopItemPageExecutor(repository, intentAction, id) },
            reducer = ShopItemPageReducer
        )
}