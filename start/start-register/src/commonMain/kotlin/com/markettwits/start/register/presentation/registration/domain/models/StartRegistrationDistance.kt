package com.markettwits.start.register.presentation.registration.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationDistance(
    val id : Int,
    val description : String,
    val title : String,
    //val additionalFields: List<StartRegistrationAdditionalField>,
    val stages : List<StartRegistrationStageWithStatement>,
    val answers : List<StartRegistrationStatementAnswer>,
    val price : StartRegistrationFieldPrice,
    val format : String,
)