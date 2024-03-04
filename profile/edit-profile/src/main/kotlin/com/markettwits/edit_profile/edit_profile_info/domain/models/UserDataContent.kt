package com.markettwits.edit_profile.edit_profile_info.domain.models

import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team

data class UserDataContent(
    val user: UserData,
    val teams: List<Team>,
    val cities: List<City>
)
