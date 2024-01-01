package com.markettwits.profile.presentation.component.edit_profile.presentation.mapper


import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.profile.presentation.component.edit_profile.models.CityUi
import com.markettwits.profile.presentation.component.edit_profile.models.TeamUi
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiState
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

interface RemoteUserToEditProfileMapper {
    // fun map(user: User): List<EditProfileUiPage>
    fun map(list: List<EditProfileUiPage>): ChangeProfileInfoRequest
    fun map(e: Exception): EditProfileUiState
    fun map(cityRemote: CityRemote, teamsRemote: TeamsRemote, user: User): EditProfileUiState
    fun mapAll(
        cityRemote: CityRemote,
        teamsRemote: TeamsRemote,
        user: User
    ): List<EditProfileUiPage>

    class Base : RemoteUserToEditProfileMapper {
//        override fun map(user: User): List<EditProfileUiPage> {
//            val mySocialNetworkPage = EditProfileUiPage.MySocialNetwork(
//                telegram = user.telegram ?: "",
//                whatsApp = user.whatsapp ?: "",
//                vk = user.vk ?: "",
//                instagram = user.instagram ?: ""
//            )
//            val userData = EditProfileUiPage.UserData(
//                name = user.name,
//                surname = user.surname,
//                sex = user.sex,
//                birthday = user.birthday,
//                email = user.email,
//                phoneNumber = user.number,
//                city = user.address,
//                team = user.team ?: ""
//            )
//            val userCustomInfo = EditProfileUiPage.MyInfo(
//                description = user.comment_for_address ?: "",
//                imageUrl = user.photo_id ?: ""
//            )
//            return listOf(mySocialNetworkPage, userData, userCustomInfo)
//        }

        override fun map(list: List<EditProfileUiPage>): ChangeProfileInfoRequest {
            val mySocialNetworkPage =
                list.find { it is EditProfileUiPage.MySocialNetwork } as? EditProfileUiPage.MySocialNetwork
                    ?: EditProfileUiPage.MySocialNetwork("", "", "", "")

            val userDataPage =
                list.find { it is EditProfileUiPage.UserData } as? EditProfileUiPage.UserData
                    ?: EditProfileUiPage.UserData(
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        emptyList(),
                        emptyList()
                    )

            val myInfoPage =
                list.find { it is EditProfileUiPage.MyInfo } as? EditProfileUiPage.MyInfo
                    ?: EditProfileUiPage.MyInfo("", "")

            return ChangeProfileInfoRequest(
                address = userDataPage.city,
                birthday = mapBirthdayStringToCloud(userDataPage.birthday),
                comment_for_address = myInfoPage.description,
                email = userDataPage.email,
                favor = null, // You need to provide the correct value
                instagram = mySocialNetworkPage.instagram,
                name = userDataPage.name,
                number = userDataPage.phoneNumber,
                photo_id = null,
                sex = userDataPage.sex,
                surname = userDataPage.surname,
                team = userDataPage.team,
                telegram = mySocialNetworkPage.telegram,
                vk = mySocialNetworkPage.vk,
                whatsapp = mySocialNetworkPage.whatsApp,
                id = userDataPage.id
            )
        }

        override fun map(e: Exception): EditProfileUiState {
            return EditProfileUiState.Error(message = e.message.toString())
        }

        override fun map(
            cityRemote: CityRemote,
            teamsRemote: TeamsRemote,
            user: User
        ): EditProfileUiState {
            return EditProfileUiState.Base(
                mapAll(
                    user = user,
                    teamsRemote = teamsRemote,
                    cityRemote = cityRemote
                )
            )
        }

        override fun mapAll(
            cityRemote: CityRemote,
            teamsRemote: TeamsRemote,
            user: User
        ): List<EditProfileUiPage> {
            val mySocialNetworkPage = EditProfileUiPage.MySocialNetwork(
                telegram = user.telegram ?: "",
                whatsApp = user.whatsapp ?: "",
                vk = user.vk ?: "",
                instagram = user.instagram ?: ""
            )
            val userData = EditProfileUiPage.UserData(
                id = user.id,
                name = user.name,
                surname = user.surname,
                sex = user.sex,
                birthday = mapBirthdayCloudToUI(user.birthday),
                email = user.email,
                phoneNumber = user.number,
                city = user.address,
                team = user.team ?: "",
                _teams = mapTeamCloudToUi(teamsRemote),
                _cities = mapCityCloudToUi(cityRemote)
            )
            val userCustomInfo = EditProfileUiPage.MyInfo(
                description = user.comment_for_address ?: "",
                imageUrl = user.photo_id ?: ""
            )
            return listOf(mySocialNetworkPage, userData, userCustomInfo)
        }

        private fun mapCityCloudToUi(cloud: CityRemote): List<CityUi> =
            cloud.rows.map { CityUi(it.id, it.name) }

        private fun mapTeamCloudToUi(cloud: TeamsRemote): List<TeamUi> =
            cloud.rows.map { TeamUi(it.id, it.name) }

        private fun mapBirthdayStringToCloud(date: String): String {
            val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val pareseDate = inputFormat.parse(date)
            val zoneId = ZoneId.of("Z")
            val zonedDateTime = ZonedDateTime.ofInstant(pareseDate.toInstant(), zoneId)
            val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            return outputFormat.format(zonedDateTime)
        }
        private fun mapBirthdayCloudToUI(date: String): String {
            val zonedDateTime =
                ZonedDateTime.parse(date)
            val localDate =
                LocalDate.from(zonedDateTime)
            val outputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            return outputFormat.format(localDate)
        }
    }
}