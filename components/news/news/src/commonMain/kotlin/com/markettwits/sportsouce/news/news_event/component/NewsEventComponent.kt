package com.markettwits.sportsouce.news.news_event.component

import com.markettwits.sportsouce.news.news_event.store.NewsEventStore
import kotlinx.coroutines.flow.StateFlow

interface NewsEventComponent {

    val state: StateFlow<NewsEventStore.State>

    fun obtainEvent(intent: NewsEventStore.Intent)
}