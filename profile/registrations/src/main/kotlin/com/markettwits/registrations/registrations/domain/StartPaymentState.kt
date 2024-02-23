package com.markettwits.registrations.registrations.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartPaymentState(
    val paymentList: List<StartOrderInfo> = emptyList(),
    val totalCost: Int = 0,
    val count: Int = 0
)