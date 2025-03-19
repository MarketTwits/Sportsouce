package com.markettwits.news_list.data

import com.markettwits.news.cloud.SportSauceNewsNetworkApi
import com.markettwits.news_list.domain.NewsInfo

class NewsDataSourceBase(
    private val service: SportSauceNewsNetworkApi,
    private val mapper: NewsRemoteToDomainMapper
) : NewsDataSource {
    override suspend fun news(): Result<List<NewsInfo>> =
        runCatching {
            mapper.map(service.news())
    }
}