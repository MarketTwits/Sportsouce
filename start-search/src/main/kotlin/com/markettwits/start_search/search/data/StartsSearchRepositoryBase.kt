package com.markettwits.start_search.search.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_search.search.data.mapper.StartsSearchToUiMapper
import com.markettwits.starts_common.domain.StartsListItem

class StartsSearchRepositoryBase(
    private val service: SportsouceApi,
    private val cloudMapper: StartsSearchToUiMapper
) : StartsSearchRepository {
    override suspend fun starts(value: String): Result<List<StartsListItem>> =
        runCatching {
            val starts = service.startWithFilter(cloudMapper.map(value))
            cloudMapper.map(starts)
        }
}