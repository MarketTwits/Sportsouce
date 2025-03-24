package com.markettwits.auth.cloud.model.change

import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoResponse(
    val message: String,
    val success: Boolean
)