package com.markettwits.sportsouce.profile.authorized.authorized.data.mapper

import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration

interface AuthorizedProfileMapper {

    fun map(
        user: User,
        userRegistries: List<UserRegistration>,
        userMembers: List<ProfileMembers.ProfileMember>
    ): UserProfile
}