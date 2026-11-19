package com.markettwits.sportsouce.start.search.search.data.repository

import com.markettwits.cahce.ObservableListCache
import com.markettwits.sportsouce.start.search.filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.sportsouce.start.search.search.domain.SearchHistory
import com.markettwits.sportsouce.start.search.search.domain.StartsSearch
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
        flow {
            val starts = startsService.startWithFilter(filterMapper.map(filter, value, sorted))
            emit(cloudMapper.map(history().first(), starts))
        }

    override suspend fun history(): Flow<List<String>> =
        cache.observe().map { it?.map { it.value } ?: emptyList() }

    override suspend fun addToHistory(value: String) {
        cache.set(value = SearchHistory(value))
    }

    override suspend fun deleteFromHistory(value: String) {
        (cache as? com.markettwits.cahce.InStorageListCache<SearchHistory>)?.remove(value = SearchHistory(value))
    }

}

