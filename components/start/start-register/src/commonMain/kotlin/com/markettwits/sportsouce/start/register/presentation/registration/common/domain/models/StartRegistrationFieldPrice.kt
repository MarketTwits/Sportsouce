package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface StartRegistrationFieldPrice{

    @Serializable
    data object Empty : StartRegistrationFieldPrice

    @Serializable
    data class Cost(val price : Int) : StartRegistrationFieldPrice
}