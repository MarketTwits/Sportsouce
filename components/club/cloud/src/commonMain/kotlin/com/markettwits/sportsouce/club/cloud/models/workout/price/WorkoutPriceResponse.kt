package com.markettwits.sportsouce.club.cloud.models.workout.price

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPriceResponse(
    @SerialName("priceWithoutDiscounts")
    val priceWithoutDiscounts: Int,
    @SerialName("totalPrice")
    val totalPrice: Int
)