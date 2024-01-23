package com.markettwits.profile.presentation.sign_up.domain

import com.markettwits.core_ui.base_extensions.retryRunCatching
import com.markettwits.core_ui.base_extensions.retryRunCatchingAsync

interface SignUpValidation {
    fun validate(statement: SignUpStatement): Result<SignUpStatement>
}

class SignUpValidationBase : SignUpValidation {
    override fun validate(statement: SignUpStatement): Result<SignUpStatement> =
        retryRunCatching {
            if (statement.name.isNotEmpty()) else throw IllegalArgumentException("Имя не должно быть пустым")
            if (statement.surname.isNotEmpty()) else throw IllegalArgumentException("Фамилия не должна быть пустой")
            if (validateRussianPhoneNumber(statement.phone)) else throw IllegalArgumentException("Формат телефона +70000000000")
            if (validateEmail(statement.email)) else throw IllegalArgumentException("Введите корректный адрес почты")
            if (statement.sex.isNotEmpty()) else throw IllegalArgumentException("Укажите свой пол")
            if (statement.birthday.isNotEmpty()) else throw IllegalArgumentException("Укажите свой день рождения")
            if (comparePassword(statement.password, statement.repeatPassword)) else throw IllegalArgumentException("Пароли не совпадают")
            statement
        }
    fun comparePassword(first: String, second: String): Boolean =
        first == second

    fun validateRussianPhoneNumber(phoneNumber: String): Boolean {
        val regexPattern = """^\+7\d{10}$"""
        return Regex(regexPattern).matches(phoneNumber)
    }

    fun validateEmail(email: String): Boolean {
        val regexPattern = """^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$"""
        return Regex(regexPattern).matches(email)
    }

}