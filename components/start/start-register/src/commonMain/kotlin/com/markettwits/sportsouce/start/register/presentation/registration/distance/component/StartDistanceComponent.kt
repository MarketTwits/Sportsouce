package com.markettwits.sportsouce.start.register.presentation.registration.distance.component

import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartStageComponent
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import kotlinx.coroutines.flow.StateFlow

interface StartDistanceComponent : StartStageComponent {

    val state: StateFlow<StartRegistrationStagePage.Registration>

    fun onClickStartMember(startStatement: StartStatement)

    fun onChangeStartStatement(startStatement: StartStatement)

    fun onChangeDistanceAnswer(startRegisterAnswer: StartRegistrationStatementAnswer)

    fun onChangeStatementAnswer(startRegisterAnswer: StartRegistrationStatementAnswer)

    fun onClickGoBack()

    fun onClickGoNext()
}
