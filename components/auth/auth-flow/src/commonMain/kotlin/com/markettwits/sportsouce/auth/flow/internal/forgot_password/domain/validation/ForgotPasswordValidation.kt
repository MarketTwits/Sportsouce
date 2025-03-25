package com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.validation

internal interface ForgotPasswordValidation {
    suspend fun map(email: String): Result<String>
}