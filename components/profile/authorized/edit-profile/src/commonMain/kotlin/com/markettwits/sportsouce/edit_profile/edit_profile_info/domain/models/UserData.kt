package com.markettwits.sportsouce.edit_profile.edit_profile_info.domain.models

import kotlinx.serialization.Serializable

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
)