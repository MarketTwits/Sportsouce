package com.markettwits.club.registration.domain

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutRegistrationForm(
    val type: RegistrationType,
    val name: String,
    val surname: String,
    val phone: String
)

@Serializable
sealed interface RegistrationType {

    val id: Int

    @Serializable
    data class Workout(override val id: Int) : RegistrationType

    @Serializable
    data class Schedule(override val id: Int) : RegistrationType

    @Serializable
    data class Trainer(override val id: Int) : RegistrationType

    @Serializable
    data class Subscription(val count: Int, override val id: Int) : RegistrationType

    @Serializable
    data object Empty : RegistrationType {
        override val id: Int = 0
    }
}