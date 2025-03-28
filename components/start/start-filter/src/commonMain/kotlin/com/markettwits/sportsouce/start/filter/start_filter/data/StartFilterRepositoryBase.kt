package com.markettwits.sportsouce.start.filter.start_filter.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.sportsouce.start.filter.start_filter.data.cache.FilterCache
import com.markettwits.sportsouce.start.filter.start_filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.sportsouce.start.filter.start_filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.sportsouce.start.filter.start_filter.domain.StartFilter
import com.markettwits.sportsouce.start.filter.start_filter.domain.StartFilterRepository
import com.markettwits.sportsouce.start.filter.start_filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapper
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

internal class StartFilterRepositoryBase(
    private val service: SportsouceApi,
    private val cache: FilterCache,
    private val execute: ExecuteWithCache,
    private val fetchMapper: StartFilterRemoteToDomainMapper,
    private val sendMapper: StartFilterDomainToRemoteMapper,
    private val startsCloudToUiMapper: StartsCloudToListMapper
) : StartFilterRepository {

    init {
        throw RuntimeException()
    }

    override suspend fun filter(): Flow<StartFilter> =
        flow {
            emit(fetchFilter())
        }

    override suspend fun starts(
        state: StartFilterUi,
        sorted: StartFilter.Sorted
    ): Result<List<StartsListItem>> =
        runCatching {
            val starts = service.startWithFilter(sendMapper.map(state))
            sendMapper.sort(sorted, startsCloudToUiMapper.mapSingle(starts.rows))
        }

    private suspend fun fetchFilter(): StartFilter {
        val (kindOfSport, seasons, cities) = coroutineScope {
            withContext(Dispatchers.Main.immediate) {
                val deferredKindOfSports = async { service.kindOfSports() }
                val deferredSeasons = async { service.seasons() }
                val deferredCities = async { service.cities(true) }
                Triple(
                    deferredKindOfSports.await(),
                    deferredSeasons.await(),
                    deferredCities.await()
                )
            }
        }
        return fetchMapper.map(kindOfSport, seasons, cities)
    }
}


