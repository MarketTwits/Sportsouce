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
    val registrationWithoutPayment : Boolean? = null,
    @SerialName("promocode")
    val promocode: String,
    @SerialName("start_id")
    val startId: Int
)