package com.markettwits.shop.catalog.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Message
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State

object ShopCatalogReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, isError = true, message = msg.message)
            is Message.Loaded -> copy(isLoading = false, isError = false, items = msg.items)
            is Message.Loading -> copy(isLoading = true, isError = false)
        }
    }
}