package com.markettwits.teams_city.data

import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.teams_city.data.cache.TeamsAndCities
import com.markettwits.teams_city.data.cache.TeamsAndCitiesCache
import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal class TeamsCityRepositoryBase(
    private val cloud: SportsouceApi,
    private val cache: TeamsAndCitiesCache,
    private val executeWithCache: ExecuteWithCache
) : TeamsCityRepository {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    override suspend fun city(): Result<List<City>> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                var resumed = false
                scope.launch {
                    executeWithCache.executeWithCache(
                        cache = cache,
                        launch = { combine().getOrThrow() },
                        callback = { result ->
                            if (!resumed) {
                                continuation.resume(Result.success(result.cities))
                                resumed = true
                            }
                        }
                    )
                }
            }
        }
    }

    override suspend fun teams(): Result<List<Team>> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                var resumed = false
                scope.launch {
                    executeWithCache.executeWithCache(
                        cache = cache,
                        launch = { combine().getOrThrow() },
                        callback = { result ->
                            if (!resumed) {
                                continuation.resume(Result.success(result.teams))
                                resumed = true
                            }
                        }
                    )
                }
            }
        }
    }

    private suspend fun combine(): Result<TeamsAndCities> =
        runCatching {
            val city = cityCloud().getOrThrow()
            val teams = teamsCloud().getOrThrow()
            TeamsAndCities(teams, city)
        }

    private suspend fun cityCloud(): Result<List<City>> = runCatching {
        cloud.cities(false).rows.map {
            City(id = it.id, name = it.name)
        }
    }

    private suspend fun teamsCloud(): Result<List<Team>> = runCatching {
        cloud.teams().rows.map {
            Team(id = it.id, name = it.name)
        }
    }


}