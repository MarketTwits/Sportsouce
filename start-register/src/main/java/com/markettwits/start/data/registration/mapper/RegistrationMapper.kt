package com.markettwits.start.data.registration.mapper

import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.start.domain.StartStatement

interface RegistrationMapper {
    fun map(
        withoutPayment : Boolean,
        user: User,
        startStatement: StartStatement,
        startDistanceInfo: DistanceInfo,
        startId: Int
    ): StartRegisterRequest
}