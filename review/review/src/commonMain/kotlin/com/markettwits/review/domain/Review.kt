package com.markettwits.review.domain

import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val news: List<NewsInfo> = emptyList(),
    val actualStarts: List<StartsListItem> = emptyList(),
    val archiveStarts: List<StartsListItem> = emptyList()
)