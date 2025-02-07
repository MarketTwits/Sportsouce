package com.markettwits.start_search.filter.data

import com.markettwits.cahce.Cache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.start_search.filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.domain.StartFilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

internal class StartFilterRepositoryBase(
    private val service: SportsouceApi,
    private val fetchMapper: StartFilterRemoteToDomainMapper,
    private val cache: Cache<StartFilter>
) : StartFilterRepository {

    override suspend fun filter(): Flow<StartFilter> =
        flow {
            emit(fetchFilter())
        }

    private suspend fun fetchFilter(): StartFilter {
        val cached = cache.get()
        return if (cached != null)
            cached
        else {
            val (kindOfSport, seasons, cities) = coroutineScope {
                withContext(Dispatchers.Main) {
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
    }
}


