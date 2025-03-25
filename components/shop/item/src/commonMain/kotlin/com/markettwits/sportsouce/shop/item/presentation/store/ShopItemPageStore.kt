package com.markettwits.sportsouce.shop.item.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.shop.item.domain.models.ShopExtraOptions
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.Intent
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.Label
import com.markettwits.sportsouce.shop.item.presentation.store.ShopItemPageStore.State

interface ShopItemPageStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val message: String,
        val shopItem: ShopItem?,
        val shopItemOptions : List<ShopExtraOptions>,
    )

    sealed interface Intent {
        data class OnClickOption(val itemId: String) : Intent
        data object OnClickRetry : Intent
        data object OnClickGoBack : Intent
        data object OnClickShare : Intent
        data object OnClickAddToFavorite : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Loaded(val item: ShopItem, val shopItemOptions: List<ShopExtraOptions>) : Message
        data class Failed(val message: String) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class UpdateItem(val shopPageItem: ShopItem) : Label
    }

}
