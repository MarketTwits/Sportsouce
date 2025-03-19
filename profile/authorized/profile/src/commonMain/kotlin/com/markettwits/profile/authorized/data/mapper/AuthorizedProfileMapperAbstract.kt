package com.markettwits.profile.authorized.data.mapper

import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.profile.authorized.domain.UserProfile
import com.markettwits.profile.cloud.model.members.ProfileMembers
import com.markettwits.profile.cloud.model.registrations.UserRegistration
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

abstract class AuthorizedProfileMapperAbstract(
    private val userRegistrationsMapper: UserRegistrationsMapper,
    private val timeMapper: TimeMapper
) : AuthorizedProfileMapper {

    protected fun mapUserInfo(user: User): UserProfile.UserInfo =
        UserProfile.UserInfo(
            name = user.name,
            surname = user.surname,
            createdAt = timeMapper.mapTime(TimePattern.Full, user.createdAt),
            status = user.comment_for_address ?: "",
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