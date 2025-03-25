package com.markettwits.sportsouce.club.cloud.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KindOfSport(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)