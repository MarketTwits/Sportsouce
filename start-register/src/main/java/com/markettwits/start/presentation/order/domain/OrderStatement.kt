package com.markettwits.start.presentation.order.domain

import com.markettwits.start.domain.StartStatement
import kotlinx.serialization.Serializable

@Serializable
data class OrderStatement(
    val distanceInfo: OrderDistance,
    val members: List<StartStatement>,
    val promo: String,
    val paymentDisabled: Boolean,
    val paymentType: String,
    val checkPolitics: Boolean = false,
    val orderPrice: OrderPrice
)

@Serializable
data class OrderDistance(
    val format: String,
    val distances: List<String>,
)

@Serializable
data class OrderPrice(
    val total: String,
    val membersCount: Int,
    val discountInCache: Int,
    val discountInPercent: Int
)