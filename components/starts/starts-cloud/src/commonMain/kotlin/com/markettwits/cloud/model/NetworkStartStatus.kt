package com.markettwits.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkStartStatus(
    @SerialName("code")
    val code: Int,
    @SerialName("name")
    val name: String
)