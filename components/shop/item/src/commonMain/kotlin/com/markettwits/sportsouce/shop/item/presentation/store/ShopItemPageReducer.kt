package com.markettwits.sportsouce.shop.item.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.Message
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.State

object ShopItemPageReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(isLoading = false, error = msg.error)
            is Message.Loaded -> copy(
                isLoading = false,
                error = null,
                shopItem = msg.item,
                shopItemOptions = msg.shopItemOptions
            )

            is Message.Loading -> copy(isLoading = true, error = null)
            is Message.SimilarProductsLoaded -> copy(similarProducts = msg.similarProducts)
        }
    }
}