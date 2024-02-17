package com.markettwits.edit_profile.edit_profile_info.domain.models

data class UserDataContent(
    val user: UserData,
    val teams: List<Team>,
    val cities: List<City>
)
