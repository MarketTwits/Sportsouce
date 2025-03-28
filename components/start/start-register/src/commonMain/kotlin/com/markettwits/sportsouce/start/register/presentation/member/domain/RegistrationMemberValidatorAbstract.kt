package com.markettwits.sportsouce.start.register.presentation.member.domain

import com.markettwits.sportsouce.start.register.domain.StartStatement

abstract class RegistrationMemberValidatorAbstract : RegistrationMemberValidator {
    protected fun validateContactPerson(startStatement: StartStatement) {
        validateBasePerson(startStatement)
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!startStatement.email.matches(emailRegex)) throw IllegalArgumentException(
            "Введите корректую почту"
        )
        if (startStatement.phone.isEmpty()) throw IllegalArgumentException(
            "Введите корректый номер телефона"
        )
    }

    protected fun validateBasePerson(startStatement: StartStatement) {
        if (startStatement.name.isEmpty()) throw IllegalArgumentException(
            "Имя не должно быть пустым"
        )
        if (startStatement.surname.isEmpty()) throw IllegalArgumentException(
            "Фамилия не должно быть пустой"
        )
        if (startStatement.birthday.isEmpty()) throw IllegalArgumentException(
            "День рождения не должен быть пустой"
        )
        if (startStatement.sex.isEmpty()) throw IllegalArgumentException(
            "Пол не должен быть пустым"
        )
        if (startStatement.city.isEmpty() || startStatement.city.length < 3) throw IllegalStateException(
            "Введите корректное название города (не менее 3 символов)"
        )
        if (startStatement.team.isEmpty()) throw IllegalArgumentException(
            "Введите корректное название команды"
        )
    }
}