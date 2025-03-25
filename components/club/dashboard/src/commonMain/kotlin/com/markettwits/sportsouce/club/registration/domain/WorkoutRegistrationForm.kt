package com.markettwits.sportsouce.club.registration.domain

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRegistrationForm(
    val type: RegistrationType,
    val name: String,
    val surname: String,
    val phone: String
) {
    companion object {
        val EMPTY = WorkoutRegistrationForm(RegistrationType.Empty, "", "", "")
    }
}

