package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistration(
    @SerialName("combo_id")
    val combo_id: Int?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("is_refunded")
    val is_refunded: Boolean,
    @SerialName("members")
    val members: List<Member>,
    @SerialName("order_number")
    val orderNumber: String?,
    @SerialName("payment")
    val payment: Int?,
    @SerialName("price")
    val price: Int?,
    @SerialName("price_of_additional_fields")
    val priceOfAdditionalFields: Int,
    @SerialName("price_without_discount")
    val priceWithoutDiscount: Int,
    @SerialName("promocode")
    val promocode: Promocode?,
    @SerialName("promocode_id")
    val promocodeId: Int?,
    @SerialName("reg_code")
    val regCode: String,
    @SerialName("sberbank_id")
    val sberbankId: String?,
    @SerialName("start")
    val start: UserRegistrationStart,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("success_payment_reason")
    val successPaymentReason: String?,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("user_id")
    val userId: Int
)