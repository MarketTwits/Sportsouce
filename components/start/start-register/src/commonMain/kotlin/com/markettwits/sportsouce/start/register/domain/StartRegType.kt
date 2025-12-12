package com.markettwits.sportsouce.start.register.domain

import kotlinx.serialization.Serializable

@Serializable
sealed class StartRegType {
    @Serializable
    data object Default : StartRegType()

    @Serializable
    data class ReReg(val startOrderId: Int) : StartRegType()
}