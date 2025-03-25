package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import com.markettwits.sportsouce.start.cloud.model.register.price.fields.StartRegisterAnswer
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationStatementAnswer(
    val field: StartRegistrationAdditionalField,
    val answer: StartRegisterAnswer
)