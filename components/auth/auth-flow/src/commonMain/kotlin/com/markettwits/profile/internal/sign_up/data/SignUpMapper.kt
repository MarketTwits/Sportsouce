package com.markettwits.profile.internal.sign_up.data

import com.markettwits.auth.cloud.model.sign_up.SignUpRequest
import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement

internal interface SignUpMapper {
    fun mapToRemote(statement: SignUpStatement): SignUpRequest
}