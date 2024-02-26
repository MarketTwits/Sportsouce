package com.markettwits.profile.presentation.component.authorized.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapper

class AuthorizedProfileMapperBase(
    userRegistrationsMapper: UserRegistrationsMapper,
    timeMapper: TimeMapper
) : AuthorizedProfileMapperAbstract(userRegistrationsMapper, timeMapper) {
    override fun map(
        user: User,
        userRegistries: List<RemoteStartsUserItem>,
        userMembers: ProfileMembers
    ): UserProfile =
        UserProfile(
            id = user.id,
            socialNetwork = mapUserSocialNetwork(user),
            activity = mapUserActivities(userRegistries, userMembers),
            userInfo = mapUserInfo(user)
        )
}