package com.markettwits.start_search.search.data.repository

import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.presentation.component.StartFilterUi
import com.markettwits.start_search.search.domain.StartsSearch
import kotlinx.coroutines.flow.Flow

interface StartsSearchRepository {
    suspend fun search(
        filter: StartFilterUi,
        sorted: StartFilter.Sorted,
        value: String,
    ): Flow<StartsSearch>
    suspend fun history(): Flow<List<String>>
    suspend fun addToHistory(value: String)
}