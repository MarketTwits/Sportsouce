package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

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