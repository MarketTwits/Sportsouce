package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloudStartStatus(
    @SerialName("code")
    val code: Int,
    @SerialName("name")
    val name: String
)