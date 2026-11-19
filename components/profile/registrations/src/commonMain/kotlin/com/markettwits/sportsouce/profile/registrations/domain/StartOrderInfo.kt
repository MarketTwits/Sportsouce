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
    val orderType: StartOrderType,
    val startTitle: String,
    val cost: String,
    val costWithoutDiscount: String = cost,
    val additionalFieldsCost: String = "",
    val isReRegistrationAvailable: Boolean = false,
    val isRefunded: Boolean = false,
    val startStatusCode: Int? = null,
    val dateEndCloud: String? = null,
    val paymentCode: Int? = null,
    val paymentGroupId: Int? = null,
    val paidKopecks: Int? = null,
    val hasUnpaidOptions: Boolean = false,
    val hasPaidOptions: Boolean = false,
)
