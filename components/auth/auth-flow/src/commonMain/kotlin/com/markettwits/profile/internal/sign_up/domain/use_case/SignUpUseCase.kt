package com.markettwits.profile.internal.sign_up.domain.use_case

import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement

internal interface SignUpUseCase {
    suspend fun registry(signUpStatement: SignUpStatement): Result<Unit>
}