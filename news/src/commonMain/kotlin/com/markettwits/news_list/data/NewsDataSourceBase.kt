package com.markettwits.news_list.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.news_list.domain.NewsInfo

class NewsDataSourceBase(
    private val service: SportsouceApi,
    private val mapper: NewsRemoteToDomainMapper
) : NewsDataSource {
    override suspend fun news(): Result<List<NewsInfo>> =
        runCatching {
            mapper.map(service.news())
    }
}