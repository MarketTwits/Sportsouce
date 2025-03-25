package com.markettwits.sportsouce.edit_profile.edit_profile_info.data

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.sportsouce.teams_city.domain.City
import com.markettwits.sportsouce.teams_city.domain.Team

interface EditProfileInfoCloudMapper {
    fun send(user: User, userData: UserData): ChangeProfileInfoRequest
    fun fetch(user: User, teams: List<Team>, cities: List<City>): UserDataContent
}