package com.markettwits.sportsouce.profile.registrations.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartOrderPrice(
    val additionalPrice : String,
    val totalPrice : String,
    val isRequired : Boolean
)