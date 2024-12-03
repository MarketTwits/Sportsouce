package com.markettwits.cloud.model.start_user.values

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationNew(
    val combo_id: Int?,
    val createdAt: String,
    val id: Int,
    val is_refunded: Boolean,
    val members: List<Member>,
    val order_number: String?,
    val payment: Int,
    val price: Int?,
    val price_of_additional_fields: Int,
    val price_without_discount: Int,
    val promocode: Promocode?,
    val promocode_id: Int?,
    val reg_code: String,
    val sberbank_id: String?,
    val start: UserRegistrationStart,
    val start_id: Int,
//    val success_payment_reason: Any,
    val updatedAt: String,
    val user_id: Int
) : UserRegistrationShared