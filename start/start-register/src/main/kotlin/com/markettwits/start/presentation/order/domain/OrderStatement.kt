package com.markettwits.start.presentation.order.domain

import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.domain.StartStatement
import kotlinx.serialization.Serializable

@Serializable
data class OrderStatement(
    val orderTitle: String,
    val distanceInfo: OrderDistance,
    val members: List<StartStatement>,
    val profileMembers: List<ProfileMember>,
    val promo: String,
    val payNow: Boolean = true,
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
    val total: Double,
    val membersCount: Int,
    val discountInCache: Double,
    val discountInPercent: Int
)