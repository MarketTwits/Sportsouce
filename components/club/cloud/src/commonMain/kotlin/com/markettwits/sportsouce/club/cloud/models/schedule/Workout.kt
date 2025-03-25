package com.markettwits.sportsouce.club.cloud.models.schedule

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String
)