package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.validation

import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpFieldErrors
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStage
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement

internal interface SignUpValidation {
    fun errors(statement: SignUpStatement, stages: Set<SignUpStage>): SignUpFieldErrors
    fun validate(statement: SignUpStatement): Result<SignUpStatement>
}

internal class SignUpValidationBase : SignUpValidation {
    private val cyrillicName = Regex("^[А-Яа-яЁё]+(?:[ -][А-Яа-яЁё]+)*$")

    override fun errors(statement: SignUpStatement, stages: Set<SignUpStage>): SignUpFieldErrors {
        val contacts = SignUpStage.FIRST in stages
        val personal = SignUpStage.SECOND in stages
        val credentials = SignUpStage.THIRD in stages
        return SignUpFieldErrors(
            phone = if (!contacts) null else when {
                statement.phone.isBlank() -> "Введите номер телефона"
                !Regex("""^\+7 \(\d{3}\) \d{3}-\d{2}-\d{2}$""").matches(statement.phone) ->
                    "Введите номер полностью: +7 (000) 000-00-00"
                else -> null
            },
            email = if (!contacts) null else when {
                statement.email.isBlank() -> "Введите адрес почты"
                !Regex("""^[^\s@]+@[^\s@.]+(?:\.[^\s@.]+)+$""").matches(statement.email) ->
                    "Введите корректный адрес, например name@mail.ru"
                else -> null
            },
            name = if (!personal) null else when {
                statement.name.isBlank() -> "Введите имя"
                !cyrillicName.matches(statement.name) -> "В имени используйте кириллицу, пробелы и дефис"
                else -> null
            },
            surname = if (!personal) null else when {
                statement.surname.isBlank() -> "Введите фамилию"
                !cyrillicName.matches(statement.surname) -> "В фамилии используйте кириллицу, пробелы и дефис"
                else -> null
            },
            sex = if (personal && statement.sex !in listOf("Мужской", "Женский")) "Выберите пол" else null,
            birthday = if (personal && statement.birthday.isBlank()) "Выберите дату рождения" else null,
            password = if (!credentials) null else when {
                statement.password.isBlank() -> "Введите пароль"
                statement.password.length < 6 -> "Пароль должен содержать минимум 6 символов"
                statement.password.any(Char::isWhitespace) -> "Пароль не должен содержать пробелов"
                else -> null
            },
            repeatPassword = if (!credentials) null else when {
                statement.repeatPassword.isBlank() -> "Повторите пароль"
                statement.password != statement.repeatPassword -> "Пароли не совпадают"
                else -> null
            },
        )
    }

    override fun validate(statement: SignUpStatement): Result<SignUpStatement> {
        val message = errors(statement, SignUpStage.entries.toSet()).firstMessage()
        return if (message == null) Result.success(statement)
        else Result.failure(IllegalArgumentException(message))
    }
}
