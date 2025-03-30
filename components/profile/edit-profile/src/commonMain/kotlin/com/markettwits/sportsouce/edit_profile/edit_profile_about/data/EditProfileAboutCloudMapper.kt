package com.markettwits.sportsouce.edit_profile.edit_profile_about.data

import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User

interface EditProfileAboutCloudMapper {
    fun send(user: User, about: String): ChangeProfileInfoRequest
    fun fetch(user: User): String
}