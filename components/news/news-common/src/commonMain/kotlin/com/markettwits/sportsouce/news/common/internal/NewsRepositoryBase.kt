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

    override suspend fun news(): Result<List<NewsItem>> = runCatching {
        val news = newsMapper.map(newsNetworkApi.news())
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

    override suspend fun hashtags(): Result<List<NewsHashtag>> = runCatching {
        val hashtags = newsNetworkApi.hashtags().map {
            NewsHashtag(
                id = it.id,
                name = it.name
            )
        }
        infoLog { "Fetch hashtags $hashtags" }
        hashtags
    }
}