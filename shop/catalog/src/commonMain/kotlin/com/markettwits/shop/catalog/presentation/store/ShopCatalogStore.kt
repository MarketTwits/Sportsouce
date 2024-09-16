package com.markettwits.shop.catalog.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.catalog.presentation.cards.ShopItem
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Intent
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.Label
import com.markettwits.shop.catalog.presentation.store.ShopCatalogStore.State

interface ShopCatalogStore : Store<Intent, State, Label> {

    data class State(
        val isLoading: Boolean,
        val isError: Boolean,
        val message: String,
        val items: List<ShopItem>,
    )

    sealed interface Intent {
        data class OnClickItem(val id: String) : Intent
    }


    sealed interface Message {
        data object Loading : Message
        data class Loaded(val items: List<ShopItem>) : Message
        data class Failed(val message: String) : Message
    }

    sealed interface Label {
        data class OnClickItem(val id: String) : Label
        data object GoBack : Label
    }

}
