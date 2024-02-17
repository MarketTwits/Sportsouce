package com.markettwits.edit_profile.edit_profile_info.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.edit_profile.edit_profile_info.domain.models.City
import com.markettwits.edit_profile.edit_profile_info.domain.models.Team
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent

class EditProfileInfoCloudMapperBase(
    private val timeMapper: TimeMapper
) : EditProfileInfoCloudMapper {
    override fun send(user: User, userData: UserData): ChangeProfileInfoRequest =
        ChangeProfileInfoRequest(
            address = user.address ?: "",
            birthday = timeMapper.mapTimeToCloud(TimePattern.ddMMMMyyyy, userData.birthday),
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
        teamsRemote: TeamsRemote,
        cityRemote: CityRemote
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
            teams = mapTeamCloudToUi(teamsRemote),
            cities = mapCityCloudToUi(cityRemote)
        )

    private fun mapCityCloudToUi(cloud: CityRemote): List<City> =
        cloud.rows.map { City(it.id, it.name) }

    private fun mapTeamCloudToUi(cloud: TeamsRemote): List<Team> =
        cloud.rows.map { Team(it.id, it.name) }

}