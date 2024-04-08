package com.markettwits.edit_profile.edit_profile.presentation

import com.markettwits.edit_profile.edit_profile.models.CityUi
import com.markettwits.edit_profile.edit_profile.models.TeamUi
import kotlinx.serialization.Serializable

@Serializable
sealed interface EditProfileUiPage {
    @Serializable
    data class MySocialNetwork(
        val telegram: String,
        val whatsApp: String,
        val vk: String,
        val instagram: String
    ) : EditProfileUiPage

    @Serializable
    data class UserData(
        val id: Int,
        val name: String,
        val surname: String,
        val sex: String,
        val birthday: String,
        val email: String,
        val phoneNumber: String,
        val city: String,
        val team: String,
        val _teams: List<TeamUi>,
        val _cities: List<CityUi>
    ) : EditProfileUiPage

    @Serializable
    data class MyInfo(
        val description: String,
        val imageUrl: String
    ) : EditProfileUiPage
}