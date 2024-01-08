package com.markettwits.news_list.data

import com.markettwits.news_list.domain.NewsInfo
import ru.alexpanov.core_network.api.SportsouceApi

class NewsDataSourceBase(
    private val service: SportsouceApi,
    private val mapper: NewsRemoteToDomainMapper
) : NewsDataSource {
    override suspend fun news(): Result<List<NewsInfo>> {
        val response = runCatching {
            mapper.map(service.news())
        }
        return response
            .onFailure {
                return Result.failure(it)
            }
            .onSuccess {
                return Result.success(it)
            }
    }
}