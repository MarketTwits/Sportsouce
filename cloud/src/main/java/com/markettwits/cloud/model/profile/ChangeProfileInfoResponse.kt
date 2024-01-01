package com.markettwits.cloud.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoResponse(
    val message: String,
    val success: Boolean
)