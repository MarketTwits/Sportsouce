package com.markettwits.start_search.filter.data

import com.markettwits.cahce.Cache
import com.markettwits.start_cloud.api.start.SportSauceStartApi
import com.markettwits.start_search.filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.domain.StartFilterRepository
import com.markettwits.teams_city.data.TeamsCityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

internal class StartFilterRepositoryBase(
    private val service: SportSauceStartApi,
    private val teamsCityRepository: TeamsCityRepository,
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
                withContext(Dispatchers.Main.immediate) {
                    val deferredKindOfSports = async { service.kindOfSports() }
                    val deferredSeasons = async { service.seasons() }
                    val deferredCities = async { teamsCityRepository.city(true) }
                    Triple(
                        deferredKindOfSports.await(),
                        deferredSeasons.await(),
                        deferredCities.await().getOrThrow()
                    )
                }
            }
            fetchMapper.map(kindOfSport, seasons, cities)
        }
    }
}


