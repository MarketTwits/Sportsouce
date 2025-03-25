package com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.use_case

internal interface ForgotPasswordUseCase {
    suspend fun resetPassword(email: String): Result<String>
}