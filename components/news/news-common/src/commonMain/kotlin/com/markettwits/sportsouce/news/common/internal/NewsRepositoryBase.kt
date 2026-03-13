package com.markettwits.sportsouce.news.common.internal

import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.infoLog
import com.markettwits.sportsouce.news.cloud.SportSauceNewsNetworkApi
import com.markettwits.sportsouce.news.common.NewsRepository
import com.markettwits.sportsouce.news.common.model.NewsCategory
import com.markettwits.sportsouce.news.common.model.NewsHashtag
import com.markettwits.sportsouce.news.common.model.NewsItem

internal class NewsRepositoryBase(
    private val newsNetworkApi: SportSauceNewsNetworkApi,
    private val newsMapper: NetworkNewsMapper
) : NewsRepository, LogTagProvider {

    override val tag: String = "NewsRepositoryBase"

    override suspend fun newsItem(id: Int): Result<NewsItem> = runCatching {
        val news = newsMapper.map(newsNetworkApi.news(newsId = id.toString()))
        infoLog { "Fetch news item $news" }
        news
    }

    override suspend fun news(
        categoryId: Int?,
        hashtag: String?,
        limit: Int,
        offset: Int,
    ): Result<List<NewsItem>> = runCatching {
        val news = newsMapper.map(
            newsNetworkApi.news(
                categoryId = categoryId,
                hashTag = hashtag,
                limit = limit,
                offset = offset,
            )
        )
        infoLog { "Fetch news $news" }
        news
    }

    override suspend fun categories(): Result<List<NewsCategory>> = runCatching {
        val categories = newsNetworkApi.categories().map {
            NewsCategory(
                id = it.id,
                name = it.name
            )
        }
        infoLog { "Fetch categories $categories" }
        categories
    }

    override suspend fun hashtags(
        limit: Int,
        offset: Int,
    ): Result<List<NewsHashtag>> = runCatching {
        val hashtags = newsNetworkApi.hashtags(limit = limit, offset = offset).map {
            NewsHashtag(
                id = it.id ?: 0,
                name = it.name ?: ""
            )
        }
        infoLog { "Fetch hashtags $hashtags" }
        hashtags
    }
}
