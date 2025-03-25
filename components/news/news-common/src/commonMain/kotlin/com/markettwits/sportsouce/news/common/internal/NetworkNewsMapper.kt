package com.markettwits.news.common.internal

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.news.cloud.model.news.NetworkNews
import com.markettwits.news.common.model.NewsHashtag
import com.markettwits.news.common.model.NewsItem

class NetworkNewsMapper(
    private val timeMapper: TimeMapper
) {
    fun map(news: NetworkNews): List<NewsItem> {
        return news.rows.map {
            val imageUrl = it.mainImage?.fullPath ?: it.images.firstOrNull()?.fullPath ?: ""
            NewsItem(
                id = it.id,
                title = it.title,
                shortDescription = it.shortDescription,
                fullDescription = it.fullDescription,
                imageUrl = imageUrl,
                createData = timeMapper.mapTime(TimePattern.FullWithEmptySpace, it.createdAt),
                hashtags = it.hashtags.map { hashTag ->
                    NewsHashtag(
                        id = hashTag.id,
                        name = hashTag.name
                    )
                }
            )
        }
    }
}