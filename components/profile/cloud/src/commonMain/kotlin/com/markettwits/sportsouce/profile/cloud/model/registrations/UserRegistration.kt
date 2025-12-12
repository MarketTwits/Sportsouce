package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistration(
    @SerialName("combo_id")
    val comboId: Int?,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("is_refunded")
    val isRefunded: Boolean? = null,
    @SerialName("members")
    val members: List<Member>? = null,
    @SerialName("order_number")
    val orderNumber: String? = null,
    @SerialName("payment")
    val payment: Int? = null,
    @SerialName("price")
    val price: Int? = null,
    @SerialName("price_of_additional_fields")
    val priceOfAdditionalFields: Int? = null,
    @SerialName("price_without_discount")
    val priceWithoutDiscount: Int? = null,
    @SerialName("promocode")
    val promocode: Promocode? = null,
    @SerialName("promocode_id")
    val promocodeId: Int? = null,
    @SerialName("reg_code")
    val regCode: String? = null,
    @SerialName("sberbank_id")
    val sberbankId: String? = null,
    @SerialName("start")
    val start: UserRegistrationStart? = null,
    @SerialName("start_id")
    val startId: Int? = null,
    @SerialName("previous_group_id")
    val previousGroupId: Int? = null,
    @SerialName("success_payment_reason")
    val successPaymentReason: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("user_id")
    val userId: Int? = null,
)