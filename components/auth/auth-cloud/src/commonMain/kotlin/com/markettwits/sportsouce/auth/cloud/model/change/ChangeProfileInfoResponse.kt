package com.markettwits.sportsouce.auth.cloud.model.change

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoResponse(
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Boolean
)