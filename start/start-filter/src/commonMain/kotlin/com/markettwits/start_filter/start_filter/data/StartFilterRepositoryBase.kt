package com.markettwits.start_filter.start_filter.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_filter.start_filter.data.cache.FilterCache
import com.markettwits.start_filter.start_filter.data.mapper.StartFilterDomainToRemoteMapper
import com.markettwits.start_filter.start_filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class StartFilterRepositoryBase(
    private val service: SportsouceApi,
    private val cache: FilterCache,
    private val execute: ExecuteWithCache,
    private val fetchMapper: StartFilterRemoteToDomainMapper,
    private val sendMapper: StartFilterDomainToRemoteMapper,
    private val startsCloudToUiMapper: StartsCloudToListMapper
) : StartFilterRepository {
    private val scope = CoroutineScope(Dispatchers.Main)

    override suspend fun filter(): Flow<Result<StartFilter>> = channelFlow {
        runCatching {
            execute.executeWithCache(
                cache = cache,
                launch = ::launchFilter,
                callback = {
                    scope.launch {
                        send(Result.success(it))
                    }
                }
            )
        }.onFailure {
            send(Result.failure(it))
        }
        awaitClose()
    }

    private suspend fun launchFilter(): StartFilter {
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

    override suspend fun starts(state: StartFilterUi): Result<List<StartsListItem>> =
        runCatching {
            val starts = service.startWithFilter(sendMapper.map(state))
            startsCloudToUiMapper.mapSingle(starts.rows)
        }


}


