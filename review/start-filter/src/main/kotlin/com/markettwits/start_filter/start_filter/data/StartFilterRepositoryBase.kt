package com.markettwits.start_filter.start_filter.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_filter.start_filter.domain.StartFilter
import com.markettwits.start_filter.start_filter.presentation.StartFilterUi
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

internal class StartFilterRepositoryBase(
    private val service: SportsouceApi,
    private val fetchMapper: StartFilterRemoteToDomainMapper,
    private val sendMapper: StartFilterDomainToRemoteMapper,
    private val startsCloudToUiMapper: StartsCloudToUiMapper
) : StartFilterRepository {

    override suspend fun filter(): Result<StartFilter> {
        val result = runCatching {
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
            fetchMapper.map(kindOfSport, seasons, cities)
        }
        return result
    }

    override suspend fun starts(state: StartFilterUi): Result<List<StartsListItem>> =
        runCatching {
            val starts = service.startWithFilter(sendMapper.map(state))
            startsCloudToUiMapper.mapSingle(starts.rows)
        }


}


