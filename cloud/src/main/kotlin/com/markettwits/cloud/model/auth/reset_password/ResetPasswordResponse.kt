package com.markettwits.cloud.model.auth.reset_password

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponse(
    val message: String,
    val id: Int
)