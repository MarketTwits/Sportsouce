package com.markettwits.sportsouce.news.news_event.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.markettwits.sportsouce.news.news_event.component.NewsEventComponent
import com.markettwits.sportsouce.news.news_event.components.NewsEventContent
import com.markettwits.sportsouce.news.news_event.store.NewsEventStore

@Composable
fun NewsEventScreen(component: NewsEventComponent) {
    val state by component.state.collectAsState()
    NewsEventContent(news = state.news, goBack = {
        component.obtainEvent(NewsEventStore.Intent.Pop)
    })
}