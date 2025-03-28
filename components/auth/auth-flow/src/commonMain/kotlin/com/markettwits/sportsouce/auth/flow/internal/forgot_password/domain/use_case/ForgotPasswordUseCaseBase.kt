package com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.use_case

import com.markettwits.sportsouce.auth.cloud.SportSauceNetworkAuthApi
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.validation.ForgotPasswordValidation

internal class ForgotPasswordUseCaseBase(
    private val service: SportSauceNetworkAuthApi,
    private val forgotPasswordValidation: ForgotPasswordValidation
) : ForgotPasswordUseCase {
    override suspend fun resetPassword(email: String): Result<String> = runCatching {
        forgotPasswordValidation.map(email).mapCatching {
            service.resetPassword(it)
        }.getOrThrow().message
    }
}