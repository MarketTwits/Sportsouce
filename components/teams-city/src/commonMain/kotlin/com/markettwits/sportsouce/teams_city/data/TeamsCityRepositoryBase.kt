package com.markettwits.sportsouce.teams_city.data

import com.markettwits.cahce.Cache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.sportsouce.teams_city.data.cache.TeamsAndCities
import com.markettwits.sportsouce.teams_city.data.network.SportSauceNetworkTeamsCityApi
import com.markettwits.sportsouce.teams_city.domain.City
import com.markettwits.sportsouce.teams_city.domain.Team
import com.markettwits.sportsouce.teams_city.domain.TeamsCityRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


internal class TeamsCityRepositoryBase(
    private val cloud: SportSauceNetworkTeamsCityApi,
    private val cache: Cache<TeamsAndCities>,
    private val executeWithCache: ExecuteWithCache
) : TeamsCityRepository {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override suspend fun cityFlow(): Flow<List<City>> = flow {
        executeWithCache.executeWithCache(
            cache = cache,
            launch = { combine().getOrThrow() },
            callback = { result ->
                emit(result.cities)
            }
        )
    }

    override suspend fun teamsFlow(): Flow<List<Team>> = flow {
        executeWithCache.executeWithCache(
            cache = cache,
            launch = { combine().getOrThrow() },
            callback = { result ->
                emit(result.teams)
            }
        )
    }

    override suspend fun city(withStarts: Boolean): Result<List<City>> {
        if (withStarts) return cityCloud(withStarts = true)

        return withContext(Dispatchers.Main.immediate) {
            suspendCoroutine { continuation ->
                var resumed = false
                scope.launch {
                    runCatching {
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
    }

    override suspend fun teams(): Result<List<Team>> {
        return withContext(Dispatchers.Main.immediate.immediate) {
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
            val city = cityCloud(withStarts = false).getOrThrow()
            val teams = teamsCloud().getOrThrow()
            TeamsAndCities(teams, city)
        }

    private suspend fun cityCloud(withStarts: Boolean): Result<List<City>> = runCatching {
        cloud.cities(withStarts).rows.map {
            City(id = it.id, name = it.name)
        }
    }.onFailure {
        if (it is CancellationException) throw it
    }

    private suspend fun teamsCloud(): Result<List<Team>> = runCatching {
        cloud.teams().rows.map {
            Team(id = it.id, name = it.name)
        }
    }


}
