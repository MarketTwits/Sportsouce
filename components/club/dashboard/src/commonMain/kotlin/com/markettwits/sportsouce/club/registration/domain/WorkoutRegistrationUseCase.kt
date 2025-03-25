package com.markettwits.sportsouce.club.registration.domain

interface WorkoutRegistrationUseCase {

    suspend fun register(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit>

    suspend fun init() : WorkoutRegistrationForm

}