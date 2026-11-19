package com.markettwits.sportsouce.club.registration.domain

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.club.common.domain.ClubRepository

class WorkoutRegistrationUseCase(
    private val repository: ClubRepository,
) {

    suspend fun register(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit> =
        validate(workoutRegistrationForm).flatMapCallback {
            repository.workoutRegistration(workoutRegistrationForm)
        }

    suspend fun init(): WorkoutRegistrationForm = repository.workoutRegistrationUserData()

    private fun validate(request: WorkoutRegistrationForm): Result<Unit> {
        if (request.name.isBlank() || request.surname.isBlank()) {
            return Result.failure(WorkoutRegistrationValidationException("Имя/Фамилия не должны быть пустыми"))
        }
        if (!request.name.isCyrillicOnly() || !request.surname.isCyrillicOnly()) {
            return Result.failure(WorkoutRegistrationValidationException("Имя и фамилия должны содержать только кириллицу"))
        }
        return if (normalizeClubPhone(request.phone) == null) {
            Result.failure(WorkoutRegistrationValidationException("Введите корректный номер телефона: +7 (000) 000-00-00"))
        } else {
            Result.success(Unit)
        }
    }

    private fun String.isCyrillicOnly(): Boolean {
        val regex = "^[А-Яа-яЁё ]+$".toRegex()
        return regex.matches(this)
    }
}

internal class WorkoutRegistrationValidationException(message: String) : IllegalArgumentException(message)
