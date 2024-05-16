package com.markettwits.club.registration.domain

data class WorkoutRegistrationForm(
    val workoutId: Int,
    val name: String,
    val surname: String,
    val phone: String
)