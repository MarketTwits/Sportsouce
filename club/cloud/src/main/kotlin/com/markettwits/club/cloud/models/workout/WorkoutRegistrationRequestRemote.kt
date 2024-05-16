package com.markettwits.club.cloud.models.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRegistrationRequestRemote(
    val workoutId: Int,
    val name: String,
    val surname: String,
    val phone: String
)