package com.markettwits.sportsouce.teams_city.data.cache

import com.markettwits.sportsouce.teams_city.domain.City
import com.markettwits.sportsouce.teams_city.domain.Team
import kotlinx.serialization.Serializable

@Serializable
internal data class TeamsAndCities(val teams: List<Team>, val cities: List<City>)