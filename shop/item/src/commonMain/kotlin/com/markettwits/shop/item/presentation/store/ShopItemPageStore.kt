package com.markettwits.shop.item.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.item.domain.models.ShopPageItem
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Intent
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.Label
import com.markettwits.shop.item.presentation.store.ShopItemPageStore.State

interface ShopItemPageStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val message: String,
        val item: ShopPageItem?,
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
        data class Loaded(val item: ShopPageItem) : Message
        data class Failed(val message: String) : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}
