package com.markettwits.sportsouce.news.news_event.component

import com.markettwits.sportsouce.news.common.model.NewsItem
import kotlinx.serialization.Serializable

@Serializable
sealed class NewsEventInput {

    @Serializable
    data class Id(val id: Int) : NewsEventInput()

    @Serializable
    data class Item(val item: NewsItem) : NewsEventInput()
}