package com.markettwits.review.presentation.store


import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.review.presentation.store.ReviewStore.Intent
import com.markettwits.review.presentation.store.ReviewStore.Label
import com.markettwits.review.presentation.store.ReviewStore.State
import com.markettwits.starts_common.domain.StartsListItem

interface ReviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickItem(val item : Int) : Intent
        data class OnClickMenu(val item : Int) : Intent
        data object Launch : Intent
    }

    data class State(
        val isLoading : Boolean = false,
        val isError : Boolean = false,
        val message : String = "",
        val actualStarts : List<StartsListItem> = emptyList(),
        val archiveStarts : List<StartsListItem> = emptyList()
    )

    sealed interface Label {
        data class OnClickItem(val item : Int) : Label
        data class OnClickMenu(val item : Int) : Label
    }
}

