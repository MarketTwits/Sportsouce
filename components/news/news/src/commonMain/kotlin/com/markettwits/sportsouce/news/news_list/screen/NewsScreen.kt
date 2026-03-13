package com.markettwits.sportsouce.news.news_list.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.collectAsLazyPagingItems
import com.markettwits.sportsouce.news.news_list.component.NewsComponent

@Composable
fun NewsScreen(
    component: NewsComponent,
    onBack: () -> Unit,
) {
    val state by component.value.collectAsState()
    val newsItems = state.newsItems.collectAsLazyPagingItems()
    val hashtagItems = state.hashtagItems.collectAsLazyPagingItems()

    NewsScreenContent(
        state = state,
        newsItems = newsItems,
        hashtagItems = hashtagItems,
        onBack = onBack,
        onIntent = component::obtainEvent
    )
}
