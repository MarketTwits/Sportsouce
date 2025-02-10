package com.markettwits.start.register.presentation.registration.common.domain.models

import com.markettwits.start.register.domain.StartStatement
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationStageWithStatement(
    val statement: StartStatement,
    val stage: StartRegistrationStage,
)