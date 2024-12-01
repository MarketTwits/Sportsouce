package com.markettwits.start.register.presentation.registration.domain.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface StartRegistrationPrice{

    @Serializable
    data object Empty : StartRegistrationPrice

    @Serializable
    data class Cost(val price : Int) : StartRegistrationPrice
}