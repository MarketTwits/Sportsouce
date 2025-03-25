package com.markettwits.sportsouce.auth.flow.internal.sign_up.data

import com.markettwits.sportsouce.auth.cloud.model.sign_up.SignUpRequest
import com.markettwits.sportsouce.auth.flow.internal.sign_up.domain.model.SignUpStatement

internal interface SignUpMapper {
    fun mapToRemote(statement: SignUpStatement): SignUpRequest
}