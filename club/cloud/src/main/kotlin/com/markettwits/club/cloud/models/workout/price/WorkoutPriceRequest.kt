package com.markettwits.club.cloud.models.workout.price

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPriceRequest(
    val count: Int,
    val id: Int,
    val type: String
)