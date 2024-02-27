package com.markettwits.teams_city.data

import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team

interface TeamsCityRepository {
    suspend fun city(): Result<List<City>>
    suspend fun teams(): Result<List<Team>>
}