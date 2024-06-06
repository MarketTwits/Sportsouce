package com.markettwits.profile.internal.sign_up.domain.validation

import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement

internal interface SignUpValidation {
    fun validate(statement: SignUpStatement): Result<SignUpStatement>
}

internal class SignUpValidationBase : SignUpValidation {
    override fun validate(statement: SignUpStatement): Result<SignUpStatement> =
        runCatching {
            if (statement.name.isNotEmpty()) else throw IllegalArgumentException("Имя не должно быть пустым")
            if (statement.surname.isNotEmpty()) else throw IllegalArgumentException("Фамилия не должна быть пустой")
            if (validateRussianPhoneNumber(statement.phone)) else throw IllegalArgumentException("Формат телефона +7 (000) 000-00-00")
            if (validateEmail(statement.email)) else throw IllegalArgumentException("Введите корректный адрес почты")
            if (statement.sex.isNotEmpty()) else throw IllegalArgumentException("Укажите свой пол")
            if (statement.birthday.isNotEmpty()) else throw IllegalArgumentException("Укажите свой день рождения")
            if (comparePassword(
                    statement.password,
                    statement.repeatPassword
                )
            ) else throw IllegalArgumentException("Пароли не совпадают")
            if (!statement.password.contains(" ")) else throw IllegalArgumentException("Пароль не должен содержать пробелов")
            statement
        }

    private fun comparePassword(first: String, second: String): Boolean = first == second

    private fun validateRussianPhoneNumber(phoneNumber: String): Boolean {
        val regexPattern = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}\$"
        return Regex(regexPattern).matches(phoneNumber)
    }

    private fun validateEmail(email: String): Boolean {
        val regexPattern = """^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$"""
        return Regex(regexPattern).matches(email)
    }

}