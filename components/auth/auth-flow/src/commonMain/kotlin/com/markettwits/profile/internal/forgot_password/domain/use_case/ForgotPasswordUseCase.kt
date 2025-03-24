package com.markettwits.profile.internal.forgot_password.domain.use_case

internal interface ForgotPasswordUseCase {
    suspend fun resetPassword(email: String): Result<String>
}