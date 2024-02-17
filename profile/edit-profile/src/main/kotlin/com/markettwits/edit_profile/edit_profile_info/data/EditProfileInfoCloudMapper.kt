package com.markettwits.edit_profile.edit_profile_info.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent

interface EditProfileInfoCloudMapper {
    fun send(user: User, userData: UserData): ChangeProfileInfoRequest
    fun fetch(user: User, teamsRemote: TeamsRemote, cityRemote: CityRemote): UserDataContent
}