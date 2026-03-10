package com.markettwits.sportsouce.starts.popular.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.popular.presentation.store.StartsPopularStore.*

interface StartsPopularStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val error: SauceError? = null,
        val starts: List<StartsListItem> = emptyList(),
    )

    sealed interface Intent {
        data object Retry : Intent
        data object OnClickBack : Intent
        data class OnClickStart(val startItem: StartsListItem) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Loaded(val starts: List<StartsListItem>) : Message
        data class Failed(val error: SauceError) : Message
    }

    sealed interface Label {
        data class OnClickStart(val startItem: StartsListItem) : Label
        data object OnClickBack : Label
    }

}
