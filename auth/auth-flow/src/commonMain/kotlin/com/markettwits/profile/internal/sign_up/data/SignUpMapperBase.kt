package com.markettwits.profile.internal.sign_up.data

import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.profile.internal.sign_in.domain.clearPhone
import com.markettwits.profile.internal.sign_up.domain.model.SignUpStatement
import com.markettwits.time.TimeMapper

internal class SignUpMapperBase(private val timeMapper: TimeMapper) : SignUpMapper {
    override fun mapToRemote(statement: SignUpStatement): SignUpRequest =
        SignUpRequest(
            birthday = timeMapper.mapTimeToCloud(time = statement.birthday),
            email = statement.email,
            kind = "phoneCheck",
            name = statement.name,
            password = statement.password,
            number = statement.phone.clearPhone(),
            sex = statement.sex,
            surname = statement.surname
        )
}