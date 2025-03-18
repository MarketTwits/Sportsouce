package com.markettwits.club.registration.data

import com.markettwits.club.common.domain.ClubRepository
import com.markettwits.club.registration.domain.WorkoutRegistrationForm
import com.markettwits.club.registration.domain.WorkoutRegistrationUseCase
import com.markettwits.core_ui.items.extensions.flatMapCallback

class WorkoutRegistrationUseCaseBase(
    private val repository: ClubRepository
) : WorkoutRegistrationUseCase {

    override suspend fun register(workoutRegistrationForm: WorkoutRegistrationForm): Result<Unit> =
        validate(workoutRegistrationForm).flatMapCallback {
            repository.workoutRegistration(workoutRegistrationForm)
        }

    private fun validate(request: WorkoutRegistrationForm): Result<Unit> {
        if (request.name.isBlank() || request.surname.isBlank()) {
            return Result.failure(Exception("Имя/Фамилия не должны быть пустыми"))
        }
        if (!request.name.isTextOnly() || !request.surname.isTextOnly()) {
            return Result.failure(Exception("Имя/Фамилия могут быть только текстом, без цифр и спецсимволов"))
        }
        return if (request.phone.isBlank() || !russianPhoneNumberIsValid(request.phone)) {
            Result.failure(Exception("Введите корректный номер телефона : +7 (000) 000-00-00"))
        } else {
            Result.success(Unit)
        }
    }

    private fun russianPhoneNumberIsValid(phoneNumber: String): Boolean {
        val regexPattern = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}\$"
        return Regex(regexPattern).matches(phoneNumber)
    }

    private fun String.isTextOnly(): Boolean {
        val regex = "^[A-Za-zА-Яа-яЁё\\s]+$".toRegex()
        return regex.matches(this)
    }
}
