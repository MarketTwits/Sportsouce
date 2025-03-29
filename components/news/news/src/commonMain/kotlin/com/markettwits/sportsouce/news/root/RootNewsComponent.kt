package com.markettwits.sportsouce.news.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_event.component.NewsEventComponent
import com.markettwits.sportsouce.news.news_list.component.NewsComponent
import kotlinx.serialization.Serializable

interface RootNewsComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object News : Config
        @Serializable
        data class NewsEvent(val news: NewsItem) : Config
    }

    sealed interface Child {
        data class News(val component: NewsComponent) : Child
        data class NewsEvent(val component: NewsEventComponent) : Child
    }
}