package com.markettwits.start.register.presentation.registration.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationStage(
    val id : Int,
    val distanceId : Int,
    val name : String,
    val length : String,
    val sex : String,
    val additionalFields : List<StartRegistrationStatementAnswer>
)