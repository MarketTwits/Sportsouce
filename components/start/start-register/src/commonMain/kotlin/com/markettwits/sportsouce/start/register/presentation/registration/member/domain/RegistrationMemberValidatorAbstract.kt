package com.markettwits.sportsouce.start.register.presentation.registration.member.domain

import com.markettwits.sportsouce.start.register.domain.StartStatement

abstract class RegistrationMemberValidatorAbstract : RegistrationMemberValidator {
    protected fun validateContactPerson(startStatement: StartStatement) {
        validateBasePerson(startStatement)
        if (!startStatement.email.isValidEmail()) throw IllegalArgumentException(
            "Введите корректую почту"
        )
        if (!startStatement.phone.isValidPhone()) throw IllegalArgumentException(
            "Введите корректый номер телефона"
        )
    }

    protected fun validateBasePerson(startStatement: StartStatement) {
        if (startStatement.name.isEmpty()) throw IllegalArgumentException(
            "Имя не должно быть пустым"
        )
        if (!startStatement.name.trim().isRussianLettersOnly()) throw IllegalArgumentException(
            "Имя должно содержать только русские буквы"
        )
        if (startStatement.surname.isEmpty()) throw IllegalArgumentException(
            "Фамилия не должно быть пустой"
        )
        if (!startStatement.surname.trim().isRussianLettersOnly()) throw IllegalArgumentException(
            "Фамилия должна содержать только русские буквы"
        )
        if (startStatement.birthday.isEmpty()) throw IllegalArgumentException(
            "День рождения не должен быть пустой"
        )
        if (startStatement.sex.isEmpty()) throw IllegalArgumentException(
            "Пол не должен быть пустым"
        )
        val age = startStatement.age.toIntOrNull()
        if (age == null || age < 1) throw IllegalArgumentException(
            "Возраст должен быть не меньше 1 года"
        )
        if (startStatement.city.isEmpty() || startStatement.city.length < 3) throw IllegalStateException(
            "Введите корректное название города (не менее 3 символов)"
        )
        if (startStatement.team.isEmpty()) throw IllegalArgumentException(
            "Введите корректное название команды"
        )
    }
}

internal fun String.isValidEmail(): Boolean {
    if (isBlank()) return false
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return matches(emailRegex)
}

internal fun String.isValidPhone(): Boolean {
    if (isBlank()) return false
    val digitsOnly = filter { it.isDigit() }
    return digitsOnly.length >= 11
}

internal fun String.isRussianLettersOnly(): Boolean {
    if (isBlank()) return false
    return matches(Regex("^[А-Яа-яЁё]+$"))
}
