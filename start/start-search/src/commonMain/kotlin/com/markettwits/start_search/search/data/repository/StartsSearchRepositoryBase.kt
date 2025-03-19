package com.markettwits.start_search.search.data.repository

import com.markettwits.cahce.ObservableListCache
import com.markettwits.start_search.filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.presentation.component.StartFilterUi
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.start_search.search.domain.SearchHistory
import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.starts_common.domain.SportSauceStartsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StartsSearchRepositoryBase(
    private val startsService: SportSauceStartsApi,
    private val cloudMapper: StartsSearchToUiMapper,
    private val filterMapper: StartFilterDomainToRemoteMapper,
    private val cache: ObservableListCache<SearchHistory>,
) : StartsSearchRepository {

    override suspend fun search(
        filter: StartFilterUi,
        sorted: StartFilter.Sorted,
        value: String,
    ): Flow<StartsSearch> =
        history().map {
            val starts = startsService.startWithFilter(filterMapper.map(filter, value))
            cloudMapper.map(it, starts)
        }

    override suspend fun history(): Flow<List<String>> =
        cache.observe().map { it?.map { it.value } ?: emptyList() }

    override suspend fun addToHistory(value: String) {
        cache.set(value = SearchHistory(value))
    }

}

