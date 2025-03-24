package com.markettwits.popular.popular.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.popular.popular.presentation.store.StartsPopularStore.Intent
import com.markettwits.popular.popular.presentation.store.StartsPopularStore.Label
import com.markettwits.popular.popular.presentation.store.StartsPopularStore.State
import com.markettwits.starts_common.domain.StartsListItem

interface StartsPopularStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val starts: List<StartsListItem> = emptyList()
    )

    sealed interface Intent {
        data object Retry : Intent
        data object OnClickBack : Intent
        data class OnClickStart(val id: Int) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Loaded(val starts: List<StartsListItem>) : Message
        data class Failed(val message: String) : Message
    }

    sealed interface Label {
        data class OnClickStart(val id: Int) : Label
        data object OnClickBack : Label
    }

}
