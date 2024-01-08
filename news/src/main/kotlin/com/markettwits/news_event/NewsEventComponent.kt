package com.markettwits.news_event

import com.arkivanov.decompose.ComponentContext
import com.markettwits.news_event.store.NewsEventStore
import com.markettwits.news_list.domain.NewsInfo
import kotlinx.coroutines.flow.StateFlow

interface NewsEventComponent {
    val state: StateFlow<NewsEventStore.State>
    fun obtainEvent(intent : NewsEventStore.Intent)
}