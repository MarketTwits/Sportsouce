package com.markettwits.start.register.presentation.registration.domain.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface StartRegistrationPriceResult {

    @Serializable
    data class Value(
        val additionalFieldsPrice: Int,
        val priceWithoutDiscount: Int,
        val totalPrice: Int
    ) : StartRegistrationPriceResult

    @Serializable
    data object Free : StartRegistrationPriceResult

    @Serializable
    data object Empty : StartRegistrationPriceResult

}