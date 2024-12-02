package com.markettwits.start.register.presentation.registration.domain.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface StartRegistrationFieldPrice{

    @Serializable
    data object Empty : StartRegistrationFieldPrice

    @Serializable
    data class Cost(val price : Int) : StartRegistrationFieldPrice
}