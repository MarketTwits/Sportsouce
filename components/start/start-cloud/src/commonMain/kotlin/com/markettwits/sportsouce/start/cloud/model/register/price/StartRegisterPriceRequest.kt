package com.markettwits.sportsouce.start.cloud.model.register.price

import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterDistance
import kotlinx.serialization.Serializable

@Serializable
data class StartRegisterPriceRequest(
    val combo_id: Int?,
    val distances: List<StartRegisterDistance>,
    val registration_without_payment : Boolean? = null,
    val promocode: String,
    val start_id: Int
)