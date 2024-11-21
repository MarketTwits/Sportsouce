package com.markettwits.club.registration.domain

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRegistrationForm(
    val type: RegistrationType,
    val name: String,
    val surname: String,
    val phone: String
)

