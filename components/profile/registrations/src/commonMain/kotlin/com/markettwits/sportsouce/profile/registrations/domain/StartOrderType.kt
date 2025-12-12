package com.markettwits.sportsouce.profile.registrations.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface StartOrderType {

    @Serializable
    data object Default : StartOrderType

    @Serializable
    data class ReRegistration(val prevGroupId: Int) : StartOrderType

    fun StartOrderType.isReReg(): Boolean = (this is ReRegistration)
}