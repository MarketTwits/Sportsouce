package com.markettwits.review.presentation.store


import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.review.domain.Review
import com.markettwits.review.presentation.store.ReviewStore.Intent
import com.markettwits.review.presentation.store.ReviewStore.Label
import com.markettwits.review.presentation.store.ReviewStore.State

interface ReviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickSearch : Intent
        data class OnClickNews(val news: NewsInfo) : Intent
        data class OnClickItem(val item : Int) : Intent
        data class OnClickMenu(val item : Int) : Intent
        data class Launch(val forced: Boolean = false) : Intent
    }

    data class State(
        val isLoading : Boolean = false,
        val isError : Boolean = false,
        val message : String = "",
        val review: Review = Review(),
    )

    sealed interface Label {
        data object OnClickSearch : Label
        data class OnClickNews(val news: NewsInfo) : Label
        data class OnClickItem(val item : Int) : Label
        data class OnClickMenu(val item : Int) : Label
    }
}

