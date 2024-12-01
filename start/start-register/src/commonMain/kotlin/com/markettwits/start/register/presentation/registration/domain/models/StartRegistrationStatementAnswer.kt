package com.markettwits.start.register.presentation.registration.domain.models

import com.markettwits.start_cloud.model.register.price.fields.StartRegisterAnswer
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationStatementAnswer(
    val field: StartRegistrationAdditionalField,
    val answer: StartRegisterAnswer
)