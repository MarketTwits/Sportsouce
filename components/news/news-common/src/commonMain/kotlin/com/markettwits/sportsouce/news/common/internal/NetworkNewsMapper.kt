package com.markettwits.sportsouce.news.common.internal

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.news.cloud.model.news.NetworkNews
import com.markettwits.sportsouce.news.cloud.model.news.NetworkNewsItem
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem

class NetworkNewsMapper(
    private val timeMapper: TimeMapper
) {

    private companion object {
        const val NewsApiBaseUrl = "https://api.sportsauce.ru"
    }

    private fun normalizeImageUrl(rawUrl: String?): String {
        val url = rawUrl?.trim().orEmpty()
        if (url.isBlank()) return ""

        val normalized = when {
            url.startsWith("http://") || url.startsWith("https://") -> url
            url.startsWith("/") -> "$NewsApiBaseUrl$url"
            else -> "$NewsApiBaseUrl/$url"
        }
        return normalized.replace(" ", "%20")
    }

    fun map(news: NetworkNewsItem): NewsItem {
        return NewsItem(
            id = news.id,
            title = news.title ?: "",
            shortDescription = news.shortDescription ?: "",
            fullDescription = news.fullDescription ?: "",
            imageUrl = normalizeImageUrl(
                news.mainImage?.fullPath
                    ?: news.mainImage?.path
                    ?: news.images?.firstOrNull()?.fullPath
                    ?: news.images?.firstOrNull()?.path
            ),
            createData = timeMapper.mapTime(TimePattern.FullWithEmptySpace, news.createdAt ?: ""),
            hashtags = news.hashtags?.map { hashTag ->
                NewsHashtag(
                    id = hashTag.id ?: 0,
                    name = hashTag.name ?: ""
                )
            } ?: emptyList()
        )
    }

    fun map(news: NetworkNews): List<NewsItem> {
        return news.rows.map { newsItem ->
            map(newsItem)
        }
    }
}
