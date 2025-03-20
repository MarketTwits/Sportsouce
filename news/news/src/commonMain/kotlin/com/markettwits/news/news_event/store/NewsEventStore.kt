package com.markettwits.news.news_event.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.news.common.model.NewsItem
import com.markettwits.news.news_event.store.NewsEventStore.Intent
import com.markettwits.news.news_event.store.NewsEventStore.Label
import com.markettwits.news.news_event.store.NewsEventStore.State

interface NewsEventStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Pop : Intent
    }

    data class State(
        val news: NewsItem
    )

    sealed interface Label {
        data object Pop : Label
    }
}
