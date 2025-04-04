package com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.use_case

import com.markettwits.core_ui.items.extensions.flatMapCallback
import com.markettwits.sportsouce.auth.flow.internal.sign_up.data.SignUpMapper
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.validation.SignUpValidation
import com.markettwits.sportsouce.auth.service.api.AuthDataSource


internal class SignUpUseCaseBase(
    private val repository: AuthDataSource,
    private val validation: SignUpValidation,
    private val mapper: SignUpMapper
) : SignUpUseCase {
    override suspend fun registry(signUpStatement: SignUpStatement): Result<Unit> =
        validation.validate(signUpStatement).flatMapCallback {
            repository.register(mapper.mapToRemote(it))
        }
}