package com.markettwits.start.register.presentation.order.domain

import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import kotlinx.serialization.Serializable

@Serializable
data class OrderStatement(
    val orderTitle: String,
    val distanceInfo: List<OrderDistance>,
    val currentOrderDistanceVisibleIndex: Int,
    val profileMembers: List<ProfileMember>,
    val promo: String,
    val payNow: Boolean = true,
    val paymentDisabled: Boolean,
    val paymentType: String,
    val checkPolitics: Boolean = false,
    val orderPrice: OrderPrice,
    val discounts: List<DistanceItem.Discount>
)

@Serializable
data class OrderDistance(
    val format: String,
    val distance: String,
    val members: List<StartStatement>,
)

@Serializable
data class OrderPrice(
    val total: Int,
    val initialTotal: Int,
//    val totalAfterCombo : Int,
    val membersCount: Int,
    val discountPromoInCache: Int,
    val discountAgeInCache: Int,
    val discountComboInCache: Int,
    val promoIsApplied: Boolean = false,
    val promoDiscountInPercent: Int
)