package com.markettwits.start_search.search.data

import com.markettwits.start_search.search.domain.StartsSearch

interface StartsSearchRepository {
    suspend fun search(value: String, addToHistory: Boolean = false): Result<StartsSearch>
    suspend fun history(): List<String>
}