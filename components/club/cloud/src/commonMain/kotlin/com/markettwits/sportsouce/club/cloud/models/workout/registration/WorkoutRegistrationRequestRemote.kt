package com.markettwits.sportsouce.club.cloud.models.workout.registration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRegistrationRequestRemote(
    @SerialName("id")
    val id: Int,
    @SerialName("type")
    val type: String,
    @SerialName("count")
    val count: Int? = null,
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("phone")
    val phone: String
)