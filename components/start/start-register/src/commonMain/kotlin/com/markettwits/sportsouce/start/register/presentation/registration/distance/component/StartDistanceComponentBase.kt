package com.markettwits.sportsouce.start.register.presentation.registration.distance.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import kotlinx.coroutines.flow.MutableStateFlow

class StartDistanceComponentBase(
    componentContext: ComponentContext,
    private val innerState: StartRegistrationStagePage.Registration,
    private val onMessage: (EventContent) -> Unit,
    private val onOpenMember: (StartRegistrationStagePage.Registration, StartStatement) -> Unit,
    private val onGoBack: (StartRegistrationStagePage) -> Unit,
    private val onGoNext: (StartRegistrationStagePage) -> Unit,
) : StartDistanceComponent, ComponentContext by componentContext {

    private val feature = instanceKeeper.getOrCreate {
        StartDistanceFeature(
            innerState = innerState,
            onMessage = onMessage
        )
    }

    override val state: MutableStateFlow<StartRegistrationStagePage.Registration> = feature.state

    override fun onClickStartMember(startStatement: StartStatement) {
        onOpenMember(state.value, startStatement)
    }

    override fun onChangeStartStatement(startStatement: StartStatement) {
        feature.onChangeStartStatement(startStatement)
    }

    override fun onChangeDistanceAnswer(startRegisterAnswer: StartRegistrationStatementAnswer) {
        feature.onChangeAnswer(startRegisterAnswer,true)
    }

    override fun onChangeStatementAnswer(startRegisterAnswer: StartRegistrationStatementAnswer) {
        feature.onChangeAnswer(startRegisterAnswer,false)
    }

    override fun onClickGoBack() {
        onGoBack(state.value)
    }

    override fun onClickGoNext() {
        feature.onClickGoNext(onGoNext = onGoNext)
    }

    override val value: StartRegistrationStagePage = innerState
}
