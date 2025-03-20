package com.markettwits.news.news_list.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.news.common.model.NewsItem


interface NewsStore : Store<NewsStore.Intent, NewsStore.State, NewsStore.Label> {

    sealed interface Intent {
        data class OnClickItem(val item : NewsItem) : Intent
        data object Launch : Intent
    }

    data class State(
        val news: List<NewsItem> = emptyList(),
        val message: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false
    )

    sealed interface Label {
        data class OnClickItem(val itemId : NewsItem) : Label
    }
}
