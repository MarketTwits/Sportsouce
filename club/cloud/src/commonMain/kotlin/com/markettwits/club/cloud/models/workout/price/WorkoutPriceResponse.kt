package com.markettwits.club.cloud.models.workout.price

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPriceResponse(
    val priceWithoutDiscounts: Int,
    val totalPrice: Int
)