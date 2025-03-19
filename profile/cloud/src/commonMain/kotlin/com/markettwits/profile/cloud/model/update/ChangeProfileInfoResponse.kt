package com.markettwits.profile.cloud.model.update

import kotlinx.serialization.Serializable

@Serializable
data class ChangeProfileInfoResponse(
    val message: String,
    val success: Boolean
)