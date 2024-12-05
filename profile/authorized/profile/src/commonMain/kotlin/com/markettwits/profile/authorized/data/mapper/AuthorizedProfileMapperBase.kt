package com.markettwits.profile.authorized.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.profile.authorized.domain.UserProfile
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