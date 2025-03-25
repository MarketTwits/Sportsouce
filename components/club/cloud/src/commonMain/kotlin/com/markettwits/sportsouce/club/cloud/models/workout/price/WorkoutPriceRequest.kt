package com.markettwits.sportsouce.club.cloud.models.workout.price

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPriceRequest(
    @SerialName("count")
    val count: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String
)