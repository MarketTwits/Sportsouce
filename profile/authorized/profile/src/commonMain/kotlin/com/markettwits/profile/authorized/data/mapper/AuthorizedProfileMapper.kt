package com.markettwits.profile.authorized.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.start_user.values.UserRegistrationOld
import com.markettwits.cloud.model.start_user.values.UserRegistrationShared
import com.markettwits.profile.authorized.domain.UserProfile

interface AuthorizedProfileMapper {

    fun map(
        user: User,
        userRegistries: List<UserRegistrationShared>,
        userMembers: List<ProfileMembers.ProfileMember>
    ): UserProfile
}