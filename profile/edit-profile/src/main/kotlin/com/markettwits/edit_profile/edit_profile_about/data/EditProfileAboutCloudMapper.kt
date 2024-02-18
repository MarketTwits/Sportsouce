package com.markettwits.edit_profile.edit_profile_about.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest

interface EditProfileAboutCloudMapper {
    fun send(user: User, about: String): ChangeProfileInfoRequest
    fun fetch(user: User): String
}