package com.markettwits.cloud_clubs.models.schedule

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val description: String,
    val id: Int,
    val type: String
)