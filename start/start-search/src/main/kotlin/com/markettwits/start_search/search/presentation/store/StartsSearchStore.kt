package com.markettwits.start_search.search.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Intent
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.Label
import com.markettwits.start_search.search.presentation.store.StartsSearchStore.State
import com.markettwits.starts_common.domain.StartsListItem

interface StartsSearchStore : Store<Intent, State, Label> {
    data class State(
        val isError: Boolean = false,
        val message: String = "",
        val query: String = "",
        val starts: List<StartsListItem> = emptyList(),
        val searchHistory: List<String> = emptyList(),
    )

    sealed interface Intent {
        data class ChangeTextFiled(val value: String) : Intent
        data class OnClickHistoryItem(val value: String) : Intent
        data class OnClickStart(val id: Int, val startTitle: String) : Intent
        data object OnClickBack : Intent
        data object OnClickFilter : Intent
        data object OnClickBrushText : Intent
    }

    sealed interface Message {
        data class ChangeTextFiled(val value: String) : Message
        data class InfoLoaded(val starts: StartsSearch) : Message
        data class InfoFailed(val message: String) : Message
        data object Brush : Message
    }

    sealed interface Label {
        data class OnClickStart(val id: Int) : Label
        data object OnClickBack : Label
        data object OnClickFilter : Label
    }

}
