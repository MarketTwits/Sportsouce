package com.markettwits.popular.popular.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.popular.popular.domain.RecentStartsFilter
import com.markettwits.popular.popular.domain.StartsPopularRepository
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class StartsPopularRepositoryBase(
    private val service: SportsouceApi,
    private val mapper: StartsCloudToListMapper,
    private val recentFilter: RecentStartsFilter
) : StartsPopularRepository {
    override suspend fun popularStarts(): Flow<List<StartsListItem>> = flow {
        val result = service.fetchActualStarts()
        val sorted = recentFilter.sortPopularStarts(mapper.mapSingle(result.rows))
        emit(sorted)
    }

}