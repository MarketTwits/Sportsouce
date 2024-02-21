package com.markettwits.profile.presentation.component.authorized.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile

interface AuthorizedProfileMapper {
    fun map(
        user: User,
        userRegistries: List<RemoteStartsUserItem>,
        userMembers: ProfileMembers
    ): UserProfile
}