package com.markettwits.start_search.search.data

import com.markettwits.cahce.ObservableListCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.start_search.search.domain.SearchHistory
import com.markettwits.start_search.search.domain.StartsSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StartsSearchRepositoryBase(
    private val service: SportsouceApi,
    private val cloudMapper: StartsSearchToUiMapper,
    private val cache: ObservableListCache<SearchHistory>,
) : StartsSearchRepository {

    override suspend fun search(value: String, addToHistory: Boolean): Flow<StartsSearch> =
        history().map {
            val starts = service.startWithFilter(cloudMapper.map(value))
            cloudMapper.map(it, starts)
        }

    override suspend fun history(): Flow<List<String>> =
        cache.observe().map { it?.map { it.value } ?: emptyList() }

    override suspend fun addToHistory(value: String) {
        cache.set(value = SearchHistory(value))
    }

}

