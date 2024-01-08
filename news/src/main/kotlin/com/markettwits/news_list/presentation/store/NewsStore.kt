package com.markettwits.news_list.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.news_list.domain.NewsInfo


interface NewsStore : Store<NewsStore.Intent, NewsStore.State, NewsStore.Label> {

    sealed interface Intent {
        data class OnClickItem(val item : NewsInfo) : Intent
        data object Launch : Intent
    }

    data class State(
        val news: List<NewsInfo> = emptyList(),
        val message: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false
    )

    sealed interface Label {
        data class OnClickItem(val itemId : NewsInfo) : Label
    }
}
