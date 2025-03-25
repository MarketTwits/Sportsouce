package com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models

import com.markettwits.sportsouce.start.register.domain.StartStatement
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationStageWithStatement(
    val statement: StartStatement,
    val stage: StartRegistrationStage,
)