package com.markettwits.profile.presentation.component.edit_profile.presentation.mapper


import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileUiPage

interface RemoteUserToEditProfileMapper {
    fun map(user: User): List<EditProfileUiPage>
    fun map(list: List<EditProfileUiPage>, user: User): User
    class Base : RemoteUserToEditProfileMapper {
        override fun map(user: User): List<EditProfileUiPage> {
            val mySocialNetworkPage = EditProfileUiPage.MySocialNetwork(
                telegram = user.telegram ?: "",
                whatsApp = user.whatsapp ?: "",
                vk = user.vk ?: "",
                instagram = user.instagram ?: ""
            )
            val userData = EditProfileUiPage.UserData(
                name = user.name,
                surname = user.surname,
                sex = user.sex,
                birthday = user.birthday,
                email = user.email,
                phoneNumber = user.number,
                city = user.address,
                team = user.team ?: ""
            )
            val userCustomInfo = EditProfileUiPage.MyInfo(
                description = user.comment_for_address ?: "",
                imageUrl = user.photo_id ?: ""
            )
            return listOf(mySocialNetworkPage, userData, userCustomInfo)
        }

        override fun map(list: List<EditProfileUiPage>, user: User): User {
            val mySocialNetworkPage =
                list.find { it is EditProfileUiPage.MySocialNetwork } as? EditProfileUiPage.MySocialNetwork
                    ?: EditProfileUiPage.MySocialNetwork("", "", "", "")

            val userDataPage =
                list.find { it is EditProfileUiPage.UserData } as? EditProfileUiPage.UserData
                    ?: EditProfileUiPage.UserData("", "", "", "", "", "", "", "")

            val myInfoPage =
                list.find { it is EditProfileUiPage.MyInfo } as? EditProfileUiPage.MyInfo
                    ?: EditProfileUiPage.MyInfo("", "")

            return User(
                address = userDataPage.city,
                age = user.age,
                birthday = userDataPage.birthday,
                comment_for_address = myInfoPage.description,
                createdAt = user.createdAt,
                email = userDataPage.email,
                favor = null, // You need to provide the correct value
                id = user.id,
                instagram = mySocialNetworkPage.instagram,
                name = userDataPage.name,
                number = userDataPage.phoneNumber,
                patronymic = user.patronymic,
                photo_id = null,
                role = user.role,
                sex = userDataPage.sex,
                surname = userDataPage.surname,
                team = userDataPage.team,
                telegram = mySocialNetworkPage.telegram,
                updatedAt = user.updatedAt,
                verification_code = user.verification_code,
                verified = user.verified,
                vk = mySocialNetworkPage.vk,
                whatsapp = mySocialNetworkPage.whatsApp
            )
        }
    }
}