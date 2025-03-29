package com.markettwits.sportsouce.review.review.domain

import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val news: List<NewsItem> = emptyList(),
    val actualStarts: List<StartsListItem> = emptyList(),
    val archiveStarts: List<StartsListItem> = emptyList()
)