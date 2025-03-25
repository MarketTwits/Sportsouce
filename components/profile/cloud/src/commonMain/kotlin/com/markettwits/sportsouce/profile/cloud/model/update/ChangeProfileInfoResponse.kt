package com.markettwits.sportsouce.profile.cloud.model.update

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoResponse(
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Boolean
)