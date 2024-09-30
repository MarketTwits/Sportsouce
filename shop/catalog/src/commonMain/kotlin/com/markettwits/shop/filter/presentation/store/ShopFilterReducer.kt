package com.markettwits.shop.filter.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.shop.catalog.domain.models.ShopFilterPrice
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.Message
import com.markettwits.shop.filter.presentation.store.ShopFilterStore.State

object ShopFilterReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.FilterLoaded -> copy(
                categories = msg.categories,
                selectedPrice = msg.price,
                isLoading = false
            )

            is Message.UpdatePrice -> copy(selectedPrice = msg.price)
            is Message.UpdateSelectedOption -> copy(selectedOptionUID = msg.selectedOptions)
            is Message.UpdateOptionsAndRestrictionPrice -> copy(
                options = msg.options,
                selectedPrice = msg.price,
                isLoading = false
            )

            is Message.Loading -> copy(isLoading = true)
            is Message.UpdateCategories -> copy(categories = msg.categories)
            is Message.UpdateCurrentPath -> copy(currentCategoryPath = msg.path)
            is Message.ResetFilter -> copy(
                currentCategoryPath = emptyList(),
                options = emptyList(),
                selectedPrice = ShopFilterPrice.EMPTY,
                selectedOptionUID = emptySet(),
                isApplied = false
            )

            is Message.ApplyFilter -> copy(isApplied = true)
        }
    }
}