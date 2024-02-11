package com.markettwits.start_search.search.data

import com.markettwits.starts_common.domain.StartsListItem

interface StartsSearchRepository {
    suspend fun starts(value: String): Result<List<StartsListItem>>
}