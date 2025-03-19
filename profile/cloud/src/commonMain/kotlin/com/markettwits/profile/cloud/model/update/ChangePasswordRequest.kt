package com.markettwits.profile.cloud.model.update

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val password : String,
    val newPassword : String
)