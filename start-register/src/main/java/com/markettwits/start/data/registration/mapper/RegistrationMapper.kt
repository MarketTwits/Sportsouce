package com.markettwits.start.data.registration.mapper


import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.order.domain.OrderStatement

interface RegistrationMapper {
    fun mapBase(
        withoutPayment: Boolean,
        user: User,
        startStatement: StartStatement,
        startDistanceItem: DistanceItem.DistanceInfo,
        startId: Int
    ): StartRegisterRequest

    fun mapCombo(
        withoutPayment: Boolean,
        user: User,
        startStatement: StartStatement,
        startDistanceItem: DistanceItem.DistanceCombo,
        startId: Int
    ): StartRegisterRequest.Combo

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