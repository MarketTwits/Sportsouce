package com.markettwits.edit_profile.edit_profile_info.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

class EditProfileInfoCloudMapperBase(
    private val timeMapper: TimeMapper
) : EditProfileInfoCloudMapper {
    override fun send(user: User, userData: UserData): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = userData.city,
            birthday = timeMapper.mapTimeToCloud(TimePattern.FullWithDots, userData.birthday),
            comment_for_address = user.comment_for_address,
            email = user.email,
            favor = null, // You need to provide the correct value
            instagram = user.instagram,
            name = userData.name,
            number = userData.phoneNumber,
            photo_id = user.photo_id,
            sex = userData.sex,
            surname = userData.surname,
            team = userData.team,
            telegram = user.telegram,
            vk = user.vk,
            whatsapp = user.whatsapp,
            id = user.id
        )

    override fun fetch(
        user: User,
        teams: List<com.markettwits.teams_city.domain.Team>,
        cities: List<com.markettwits.teams_city.domain.City>
    ): UserDataContent =
        UserDataContent(
            user = UserData(
                id = user.id,
                name = user.name,
                surname = user.surname,
                sex = user.sex,
                birthday = timeMapper.mapTime(TimePattern.FullWithDots, user.birthday),
                email = user.email,
                phoneNumber = user.number,
                city = user.address ?: "",
                team = user.team ?: ""
            ),
            teams = teams,
            cities = cities
        )

}