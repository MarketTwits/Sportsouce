package com.markettwits.news_event.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.news_event.store.NewsEventStore.Intent
import com.markettwits.news_event.store.NewsEventStore.Label
import com.markettwits.news_event.store.NewsEventStore.State

interface NewsEventStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object Pop : Intent
    }

    data class State(
        val news: NewsInfo
    )

    sealed interface Label {
        data object Pop : Label
    }
}
