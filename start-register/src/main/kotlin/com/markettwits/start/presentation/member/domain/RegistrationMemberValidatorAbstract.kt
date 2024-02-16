package com.markettwits.start.presentation.member.domain

import android.util.Patterns
import com.markettwits.start.domain.StartStatement

abstract class RegistrationMemberValidatorAbstract : RegistrationMemberValidator {
    protected fun validateContactPerson(startStatement: StartStatement) {
        validateBasePerson(startStatement)
        if (!Patterns.EMAIL_ADDRESS.matcher(startStatement.email)
                .matches()
        ) throw IllegalArgumentException(
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
        if (startStatement.city.isEmpty() || startStatement.city.length < 5) throw IllegalStateException(
            "Введите корректное название города (не менее 5 символов)"
        )
        if (startStatement.team.isEmpty()) throw IllegalArgumentException(
            "Введите корректное название команды"
        )
    }
}