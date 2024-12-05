package com.markettwits.cloud.model.change_password

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val password : String,
    val newPassword : String
)