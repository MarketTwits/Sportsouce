package com.markettwits.teams_city.data.cache

import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team
import kotlinx.serialization.Serializable

@Serializable
data class TeamsAndCities(val teams: List<Team>, val cities: List<City>)