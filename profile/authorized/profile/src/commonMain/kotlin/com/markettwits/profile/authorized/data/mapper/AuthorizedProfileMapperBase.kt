package com.markettwits.profile.authorized.data.mapper

import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.profile.authorized.domain.UserProfile
import com.markettwits.profile.cloud.model.members.ProfileMembers
import com.markettwits.profile.cloud.model.registrations.UserRegistration
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.time.TimeMapper

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