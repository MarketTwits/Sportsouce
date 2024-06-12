package com.markettwits.club.cloud.models.workout

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRegistrationRequestRemote(
    val id: Int,
    val type: String,
    val count: Int? = null,
    val name: String,
    val surname: String,
    val phone: String
)