package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import com.markettwits.sportsouce.start.register.domain.StartRegType
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationInfo(
    val startId: Int,
    val startTitle: String,
    val comboId : Int? = null,
    val paymentType: String,
    val promo : String = "",
    val isPaymentDisabled: Boolean,
    val regType: StartRegType,
)