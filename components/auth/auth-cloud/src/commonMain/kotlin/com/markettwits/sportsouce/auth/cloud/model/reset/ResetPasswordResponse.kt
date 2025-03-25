package com.markettwits.sportsouce.auth.cloud.model.reset

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponse(
    @SerialName("message")
    val message: String,
    @SerialName("id")
    val id: Int
)