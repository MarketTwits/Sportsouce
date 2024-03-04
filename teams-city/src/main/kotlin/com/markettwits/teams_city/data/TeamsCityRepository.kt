package com.markettwits.teams_city.data

import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team
import kotlinx.coroutines.flow.Flow

interface TeamsCityRepository {
    suspend fun cityFlow(): Flow<List<City>>
    suspend fun teamsFlow(): Flow<List<Team>>
    suspend fun city(): Result<List<City>>
    suspend fun teams(): Result<List<Team>>
}