package com.markettwits.auth.cloud.model.reset

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponse(
    val message: String,
    val id: Int
)