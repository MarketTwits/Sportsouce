package com.markettwits.start.register.presentation.registration.presentation.component.registration

import com.markettwits.start_cloud.model.start.fields.DistinctDistance
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