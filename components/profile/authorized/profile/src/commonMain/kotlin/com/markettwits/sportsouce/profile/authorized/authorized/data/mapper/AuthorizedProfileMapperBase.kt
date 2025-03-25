package com.markettwits.sportsouce.profile.authorized.authorized.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.registrations.list.data.mapper.UserRegistrationsMapper

class AuthorizedProfileMapperBase(
    userRegistrationsMapper: UserRegistrationsMapper,
    timeMapper: TimeMapper
) : AuthorizedProfileMapperAbstract(
    userRegistrationsMapper = userRegistrationsMapper,
    timeMapper = timeMapper
) {

    override fun map(
        user: User,
        userRegistries: List<UserRegistration>,
        userMembers: List<ProfileMembers.ProfileMember>
    ): UserProfile = UserProfile(
        id = user.id,
        socialNetwork = mapUserSocialNetwork(user),
        activity = mapUserActivities(userRegistries, userMembers),
        userInfo = mapUserInfo(user)
    )
}