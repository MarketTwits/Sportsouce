package com.markettwits.review.domain

import com.markettwits.news.common.model.NewsItem
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val news: List<NewsItem> = emptyList(),
    val actualStarts: List<StartsListItem> = emptyList(),
    val archiveStarts: List<StartsListItem> = emptyList()
)