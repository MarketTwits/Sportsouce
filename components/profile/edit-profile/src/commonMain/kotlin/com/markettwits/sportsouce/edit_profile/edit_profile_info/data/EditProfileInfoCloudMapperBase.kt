package com.markettwits.sportsouce.edit_profile.edit_profile_info.data

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.sportsouce.teams_city.domain.City
import com.markettwits.sportsouce.teams_city.domain.Team

class EditProfileInfoCloudMapperBase(
    private val timeMapper: TimeMapper
) : EditProfileInfoCloudMapper {
    override fun send(user: User, userData: UserData): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = userData.city,
            birthday = timeMapper.mapTimeToCloud(TimePattern.FullWithDots, userData.birthday),
            commentForAddress = user.commentForAddress,
            email = user.email ?: "",
            favor = null, // You need to provide the correct value
            instagram = user.instagram,
            name = userData.name,
            number = userData.phoneNumber,
            photoId = user.photoId,
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
        teams: List<Team>,
        cities: List<City>
    ): UserDataContent =
        UserDataContent(
            user = UserData(
                id = user.id,
                name = user.name,
                surname = user.surname,
                sex = user.sex,
                birthday = timeMapper.mapTime(TimePattern.FullWithDots, user.birthday),
                email = user.email ?: "",
                phoneNumber = user.number,
                city = user.address ?: "",
                team = user.team ?: ""
            ),
            teams = teams,
            cities = cities
        )

}