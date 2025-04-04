package com.markettwits.sportsouce.review.review.presentation.store


import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore.Intent
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore.Label
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore.State

interface ReviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object OnClickSearch : Intent
        data object OnClickSettings : Intent
        data class OnClickNews(val news: NewsItem) : Intent
        data class OnClickItem(val item : Int) : Intent
        data class OnClickMenu(val item : Int) : Intent
        data object OnClickTelegram : Intent
        data object OnClickVk : Intent
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
        data object OnClickSettings : Label
        data class OnClickNews(val news: NewsItem) : Label
        data class OnClickItem(val item : Int) : Label
        data class OnClickMenu(val item : Int) : Label
    }
}

