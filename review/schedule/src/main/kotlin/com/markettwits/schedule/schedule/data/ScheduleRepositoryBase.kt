package com.markettwits.schedule.schedule.data

import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.schedule.schedule.data.cache.StartsScheduleCacheContent
import com.markettwits.schedule.schedule.domain.ScheduleRepository
import com.markettwits.starts_common.data.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


internal class ScheduleRepositoryBase(
    private val service: SportsouceApi,
    private val startRemoteToUiMapper: StartsCloudToListMapper,
    private val cache: ObservableCache<StartsScheduleCacheContent>,
    private val executeListWithCache: ExecuteWithCache
) : ScheduleRepository {

    override suspend fun schedule(forced: Boolean): Flow<List<StartsListItem>> =
        executeListWithCache.executeWithCache(
            forced = forced,
            cache = cache,
            launch = ::launch
        ).map { it.items }

    private suspend fun launch(): StartsScheduleCacheContent {
        val items = startRemoteToUiMapper.mapSingle(
            service.startWithFilter(mapOf("maxResultCount" to "1000")).rows
        )
        return StartsScheduleCacheContent(items)
    }

}

