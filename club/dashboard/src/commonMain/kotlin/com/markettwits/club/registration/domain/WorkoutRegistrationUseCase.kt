package com.markettwits.club.registration.domain

interface WorkoutRegistrationUseCase {
    suspend fun register(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit>
}