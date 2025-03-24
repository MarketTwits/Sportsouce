package com.markettwits.club.cloud.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val description: String,
    val id: Int,
    val type: String
)