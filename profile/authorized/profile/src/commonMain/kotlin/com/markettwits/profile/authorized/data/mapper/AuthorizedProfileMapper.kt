package com.markettwits.profile.authorized.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.profile.authorized.domain.UserProfile

interface AuthorizedProfileMapper {

    fun map(
        user: User,
        userRegistries: List<UserRegistration>,
        userMembers: List<ProfileMembers.ProfileMember>
    ): UserProfile
}