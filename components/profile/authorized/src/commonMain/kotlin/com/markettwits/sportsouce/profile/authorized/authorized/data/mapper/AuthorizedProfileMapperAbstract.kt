package com.markettwits.sportsouce.profile.authorized.authorized.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.registrations.list.data.mapper.UserRegistrationsMapper

abstract class AuthorizedProfileMapperAbstract(
    private val userRegistrationsMapper: UserRegistrationsMapper,
    private val timeMapper: TimeMapper
) : AuthorizedProfileMapper {

    protected fun mapUserInfo(user: User): UserProfile.UserInfo =
        UserProfile.UserInfo(
            name = user.name,
            surname = user.surname,
            createdAt = timeMapper.mapTime(TimePattern.Full, user.createdAt),
            status = user.commentForAddress ?: "",
            photo = when (val image = user.photo) {
                is User.Photo.EmptyPhoto -> ""
                is User.Photo.WithPhoto -> image.imageUrl()
                null -> ""
            }
        )

    protected fun mapUserSocialNetwork(user: User): UserProfile.SocialNetwork =
        UserProfile.SocialNetwork(
            instagram = user.instagram ?: "",
            telegram = user.telegram ?: "",
            whatsapp = user.whatsapp ?: "",
            vk = user.vk ?: ""
        )

    protected fun mapUserActivities(
        userRegistries: List<UserRegistration>,
        userMembers: List<ProfileMembers.ProfileMember>
    ): UserProfile.Activity =
        UserProfile.Activity(
            userRegistry = userRegistrationsMapper.map(userRegistries),
            userMemberCount = userMembers.size
        )
}