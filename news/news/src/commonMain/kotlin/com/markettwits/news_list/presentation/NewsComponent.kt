package com.markettwits.news_list.presentation

import com.markettwits.news_list.presentation.store.NewsStore
import kotlinx.coroutines.flow.StateFlow

interface NewsComponent {
    fun obtainEvent(event: NewsStore.Intent)
    val value: StateFlow<NewsStore.State>
}