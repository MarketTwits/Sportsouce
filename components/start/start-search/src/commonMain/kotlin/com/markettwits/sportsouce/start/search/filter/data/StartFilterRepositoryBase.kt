package com.markettwits.sportsouce.start.search.filter.data

import com.markettwits.cahce.Cache
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.search.filter.data.mapper.StartFilterRemoteToDomainMapper
import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.domain.StartFilterRepository
import com.markettwits.sportsouce.teams_city.domain.TeamsCityRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class StartFilterRepositoryBase(
    private val service: SportSauceStartApi,
    private val teamsCityRepository: TeamsCityRepository,
    private val fetchMapper: StartFilterRemoteToDomainMapper,
    private val cache: Cache<StartFilter>,
) : StartFilterRepository {

    override suspend fun filter(): Flow<StartFilter> = flow {
        emit(cache.get() ?: fetchFilter().also { cache.set(value = it) })
    }

    private suspend fun fetchFilter(): StartFilter = coroutineScope {
        val sports = async { service.kindOfSports() }
        val seasons = async { service.seasons() }
        val cities = async { teamsCityRepository.city(withStarts = true).getOrThrow() }
        val organizers = async { fetchOrganizers() }
        fetchMapper.map(sports.await(), seasons.await(), cities.await()).copy(organizers = organizers.await())
    }

    private suspend fun fetchOrganizers(): List<String> {
        val names = linkedMapOf<String, String>()
        var offset = 0
        do {
            val page = service.organizers(offset, limit = 100)
            if (page.rows.isEmpty()) break
            page.rows.forEach { row ->
                val name = row.name?.trim().orEmpty()
                if (name.isNotEmpty()) names.getOrPut(name.lowercase()) { name }
            }
            offset += page.rows.size
        } while (offset < page.count)
        return names.values.sortedBy { it.lowercase() }
    }
}
