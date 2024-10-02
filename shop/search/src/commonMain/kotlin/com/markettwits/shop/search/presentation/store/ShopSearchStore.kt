package com.markettwits.shop.search.presentation.store


import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Intent
import com.markettwits.shop.search.presentation.store.ShopSearchStore.Label
import com.markettwits.shop.search.presentation.store.ShopSearchStore.State


interface ShopSearchStore : Store<Intent, State, Label> {

    data class State(
        val query: String = "",
        val historyItems: List<String> = emptyList()
    )

    sealed interface Intent {
        data class OnClickHistoryItem(val value: String) : Intent
        data class OnUpdateQuery(val query : String) : Intent
        data object OnClearQuery : Intent
        data object OnClickGoBack : Intent
        data object OnApplyQuery : Intent
    }

    sealed interface Message {
        data class ShowHistoryItems(val items: List<String>) : Message
        data class QueryWasUpdated(val query: String) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data class OnApplyQuery(val query: String) : Label
    }

}
