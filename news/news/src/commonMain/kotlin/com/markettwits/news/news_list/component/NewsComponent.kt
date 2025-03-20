package com.markettwits.news.news_list.component

import com.markettwits.news.news_list.store.NewsStore
import kotlinx.coroutines.flow.StateFlow

interface NewsComponent {
    fun obtainEvent(event: NewsStore.Intent)
    val value: StateFlow<NewsStore.State>
}