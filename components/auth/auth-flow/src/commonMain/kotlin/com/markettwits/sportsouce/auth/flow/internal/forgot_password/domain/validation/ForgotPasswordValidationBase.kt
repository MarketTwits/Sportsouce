package com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.validation

internal class ForgotPasswordValidationBase : ForgotPasswordValidation {
    override suspend fun map(email: String): Result<String> = runCatching {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        if (!email.matches(emailRegex)) {
            throw IllegalArgumentException("Введите корректный email")
        } else {
            email
        }
    }
}