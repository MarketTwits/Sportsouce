package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationDistance(
    val id : Int,
    val description : String,
    val title : String,
    val stages : List<StartRegistrationStageWithStatement>,
    val answers : List<StartRegistrationStatementAnswer>,
    val price : StartRegistrationFieldPrice,
    val format : String,
)