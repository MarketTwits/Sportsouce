package com.markettwits.profile.data.mapper

import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.profile.presentation.sign_up.domain.SignUpStatement

interface SignUpMapper {
    fun mapToRemote(statement: SignUpStatement): SignUpRequest
}

class SignUpMapperBase : SignUpMapper {
    override fun mapToRemote(statement: SignUpStatement): SignUpRequest =
        SignUpRequest(
            birthday = statement.birthday,
            email = statement.email,
            kind = "phoneCheck",
            name = statement.name,
            password = statement.password,
            number = statement.phone,
            sex = statement.sex,
            surname = statement.surname
        )
}