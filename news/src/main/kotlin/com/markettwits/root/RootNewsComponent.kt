package com.markettwits.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.news_event.NewsEventComponent
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.news_list.presentation.NewsComponent
import kotlinx.serialization.Serializable

interface RootNewsComponent {
    val childStack: Value<ChildStack<*, Child>>
    @Serializable
    sealed interface Config {
        @Serializable
        data object News : Config
        @Serializable
        data class NewsEvent(val news: NewsInfo) : Config
    }

    sealed interface Child {
        data class News(val component: NewsComponent) : Child
        data class NewsEvent(val component: NewsEventComponent) : Child
    }
}