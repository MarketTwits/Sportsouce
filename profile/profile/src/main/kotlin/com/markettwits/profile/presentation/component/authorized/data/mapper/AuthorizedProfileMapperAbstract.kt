package com.markettwits.profile.presentation.component.authorized.data.mapper

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.profile.presentation.component.authorized.domain.UserProfile
import com.markettwits.registrations.registrations_list.data.mapper.UserRegistrationsMapper

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
        userRegistries: List<RemoteStartsUserItem>,
        userMembers: ProfileMembers
    ): UserProfile.Activity =
        UserProfile.Activity(
            userRegistry = userRegistrationsMapper.map(userRegistries),
            userMemberCount = userMembers.count
        )
}