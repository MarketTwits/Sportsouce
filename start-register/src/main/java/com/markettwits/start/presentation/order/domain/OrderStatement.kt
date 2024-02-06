package com.markettwits.start.presentation.order.domain

import com.markettwits.start.domain.StartStatement

data class OrderStatement(
    val distanceInfo: OrderDistance,
    val members: List<StartStatement>,
    val promo: String,
    val withPayment: Boolean,
    val orderPrice: OrderPrice
)

data class OrderDistance(
    val format: String,
    val distances: List<String>,
)

data class OrderPrice(
    val total: String,
    val membersCount: Int,
    val discountInCache: Int,
    val discountInPercent: Int
)