package com.markettwits.start_filter.start_filter.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_filter.start_filter.data.cache.FilterCache
import com.markettwits.start_filter.start_filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.start_filter.start_filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.domain.StartFilterRepository
import com.markettwits.start_filter.start_filter.presentation.component.StartFilterUi
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
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
            withContext(Dispatchers.IO) {
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


