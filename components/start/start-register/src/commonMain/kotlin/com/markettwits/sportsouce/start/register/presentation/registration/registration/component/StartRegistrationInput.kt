package com.markettwits.sportsouce.start.register.presentation.registration.registration.component

import com.markettwits.sportsouce.start.cloud.model.start.fields.DistinctDistance
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationInput(
    val startId: Int,
    val startTitle: String,
    val comboId : Int? = null,
    val paymentType: String,
    val distances: List<DistinctDistance>,
    val isPaymentDisabled: Boolean,
)