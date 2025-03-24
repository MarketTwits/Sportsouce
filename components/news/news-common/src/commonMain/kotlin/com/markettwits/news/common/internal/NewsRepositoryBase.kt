package com.markettwits.news.common.internal

import com.markettwits.news.cloud.SportSauceNewsNetworkApi
import com.markettwits.news.common.NewsRepository
import com.markettwits.news.common.model.NewsCategory
import com.markettwits.news.common.model.NewsHashtag
import com.markettwits.news.common.model.NewsItem

internal class NewsRepositoryBase(
    private val newsNetworkApi: SportSauceNewsNetworkApi,
    private val newsMapper: NetworkNewsMapper
) : NewsRepository {
    override suspend fun news(): Result<List<NewsItem>> = runCatching {
        newsMapper.map(newsNetworkApi.news())
    }

    override suspend fun categories(): Result<List<NewsCategory>> = runCatching {
        newsNetworkApi.categories().map {
            NewsCategory(
                id = it.id,
                name = it.name
            )
        }
    }

    override suspend fun hashtags(): Result<List<NewsHashtag>> = runCatching {
        newsNetworkApi.hashtags().map {
            NewsHashtag(
                id = it.id,
                name = it.name
            )
        }
    }
}