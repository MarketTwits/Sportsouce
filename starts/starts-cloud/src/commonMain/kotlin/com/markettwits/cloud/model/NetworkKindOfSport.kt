package com.markettwits.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkKindOfSport(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)