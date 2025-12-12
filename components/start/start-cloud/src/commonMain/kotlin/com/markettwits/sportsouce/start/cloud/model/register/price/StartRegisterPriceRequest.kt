package com.markettwits.sportsouce.start.cloud.model.register.price

import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterDistance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterPriceRequest(
    @SerialName("combo_id")
    val comboId: Int?,
    @SerialName("distances")
    val distances: List<StartRegisterDistance>,
    @SerialName("registration_without_payment")
    val registrationWithoutPayment: Boolean? = null,
    @SerialName("is_re_registration")
    val isReRegistration: Boolean? = null,
    @SerialName("previous_group_id")
    val previousOrderId: Int? = null,
    @SerialName("promocode")
    val promocode: String,
    @SerialName("start_id")
    val startId: Int,
)