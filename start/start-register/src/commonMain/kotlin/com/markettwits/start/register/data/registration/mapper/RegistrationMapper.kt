package com.markettwits.start.register.data.registration.mapper


import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.start.register.presentation.order.domain.OrderStatement

interface RegistrationMapper {

    fun mapNewBase(
        withoutPayment: Boolean,
        user: User,
        startStatement: OrderStatement,
        startDistanceItem: DistanceItem.DistanceInfo,
        startId: Int
    ): StartRegisterRequest

    fun mapNewCombo(
        withoutPayment: Boolean,
        user: User,
        startStatement: OrderStatement,
        startDistanceItem: DistanceItem.DistanceCombo,
        startId: Int
    ): StartRegisterRequest.Combo
}