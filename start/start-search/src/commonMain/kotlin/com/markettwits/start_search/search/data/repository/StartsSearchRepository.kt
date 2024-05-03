package com.markettwits.start_search.search.data.repository

import com.markettwits.start_search.search.domain.StartsSearch
import kotlinx.coroutines.flow.Flow

interface StartsSearchRepository {
    suspend fun search(value: String, addToHistory: Boolean = false): Flow<StartsSearch>
    suspend fun history(): Flow<List<String>>
    suspend fun addToHistory(value: String)
}