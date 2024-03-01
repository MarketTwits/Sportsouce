package com.markettwits.start_search.search.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_search.search.data.local.SearchHistoryCache
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.start_search.search.domain.StartsSearch

class StartsSearchRepositoryBase(
    private val service: SportsouceApi,
    private val cloudMapper: StartsSearchToUiMapper,
    // private val cache: SearchHistoryCache,
) : StartsSearchRepository {
    // private val cache = SearchHistoryCache()
    override suspend fun search(value: String, addToHistory: Boolean): Result<StartsSearch> =
        runCatching {
            val cache = SearchHistoryCache()
            if (addToHistory)
                cache.add(value)
            val starts = service.startWithFilter(cloudMapper.map(value))
            cloudMapper.map(history(), starts)
        }

    override suspend fun history(): List<String> {
        val cache = SearchHistoryCache()
        return runCatching {
            emptyList<String>()
            cache.getList().map { it.value }
        }.getOrElse { emptyList() }
    }

}

