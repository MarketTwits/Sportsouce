package com.markettwits.registrations.list.domain

import kotlinx.serialization.Serializable

@Serializable
data class StartOrderPrice(
    val additionalPrice : String,
    val totalPrice : String,
    val isRequired : Boolean
)