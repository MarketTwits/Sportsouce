package com.markettwits.sportsouce.profile.registrations.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartOrderInfo(
    val id: Int,
    val startId: Int,
    val name: String,
    val image: String,
    val dateStartPreview: String,
    val dateStartCloud: String,
    val promo : String,
    val payment: StartOrderPaymentStatus,
    val members: List<StartOrderMember>,
    val startTitle: String,
    val cost: String,
    val costWithoutDiscount: String = cost,
    val additionalFieldsCost: String = "",
)