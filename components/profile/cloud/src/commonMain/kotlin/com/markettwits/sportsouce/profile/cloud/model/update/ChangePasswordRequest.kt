package com.markettwits.sportsouce.profile.cloud.model.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @SerialName("password")
    val password : String,
    @SerialName("newPassword")
    val newPassword : String
)