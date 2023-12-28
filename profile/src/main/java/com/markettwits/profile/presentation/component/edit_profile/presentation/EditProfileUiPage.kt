package com.markettwits.profile.presentation.component.edit_profile.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface EditProfileUiPage {
    @Serializable
    data class MySocialNetwork(
        val telegram : String,
        val whatsApp : String,
        val vk : String,
        val instagram : String
    ) : EditProfileUiPage
    @Serializable
   data class UserData(
       val name : String,
       val surname : String,
       val sex : String,
       val birthday : String,
       val email : String,
       val phoneNumber : String,
       val city : String,
       val team : String
   ) : EditProfileUiPage
    @Serializable
    data class MyInfo(
        val description : String,
        val imageUrl : String
    ) : EditProfileUiPage
}