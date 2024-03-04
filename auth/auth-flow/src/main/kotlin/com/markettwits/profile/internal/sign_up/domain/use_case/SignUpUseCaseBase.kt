package com.markettwits.profile.internal.sign_up.domain.use_case

import com.markettwits.core_ui.result.flatMapCallback
import com.markettwits.profile.api.AuthDataSource
import com.markettwits.profile.internal.sign_up.data.SignUpMapper
import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.profile.internal.sign_up.domain.validation.SignUpValidation


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