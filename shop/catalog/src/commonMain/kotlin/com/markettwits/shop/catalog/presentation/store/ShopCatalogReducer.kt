package com.markettwits.shop.catalog.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Message
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State
import com.markettwits.shop.filter.presentation.store.ShopFilterStore

internal object ShopCatalogReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Loaded -> copy(shopItems = msg.items)
            is Message.UpdateCategories -> copy()
            is Message.QueryApplied -> copy(queryState = msg.query)
            is Message.ChangeFilterVisibility -> copy(isShowFilter = msg.isShowFilter)
        }
    }
}