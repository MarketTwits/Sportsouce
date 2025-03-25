package com.markettwits.sportsouce.starts.popular.data

import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.popular.domain.RecentStartsFilter
import com.markettwits.sportsouce.starts.popular.domain.StartsPopularRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class StartsPopularRepositoryBase(
    private val service: SportSauceStartsApi,
    private val recentFilter: RecentStartsFilter
) : StartsPopularRepository {
    override suspend fun popularStarts(): Flow<List<StartsListItem>> = flow {
        val result = service.fetchActualStarts()
        val sorted = recentFilter.sortPopularStarts(result)
        emit(sorted)
    }

}