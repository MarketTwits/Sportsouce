package com.markettwits.profile.authorized.data.mapper

import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.profile.authorized.domain.UserProfile
import com.markettwits.profile.cloud.model.members.ProfileMembers
import com.markettwits.profile.cloud.model.registrations.UserRegistration

interface AuthorizedProfileMapper {

    fun map(
        user: User,
        userRegistries: List<UserRegistration>,
        userMembers: List<ProfileMembers.ProfileMember>
    ): UserProfile
}