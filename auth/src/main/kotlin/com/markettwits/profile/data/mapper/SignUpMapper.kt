package com.markettwits.profile.data.mapper

import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.profile.presentation.sign_up.domain.SignUpStatement

interface SignUpMapper {
    fun mapToRemote(statement: SignUpStatement): SignUpRequest
}

class SignUpMapperBase(private val timeMapper: TimeMapper) : SignUpMapper {
    override fun mapToRemote(statement: SignUpStatement): SignUpRequest =
        SignUpRequest(
            birthday = timeMapper.mapTimeToCloud(time = statement.birthday),
            email = statement.email,
            kind = "phoneCheck",
            name = statement.name,
            password = statement.password,
            number = statement.phone,
            sex = statement.sex,
            surname = statement.surname
        )
}