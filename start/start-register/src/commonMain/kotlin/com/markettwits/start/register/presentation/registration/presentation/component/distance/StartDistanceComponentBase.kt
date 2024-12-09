package com.markettwits.start.register.presentation.registration.presentation.component.distance

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.getOrCreateKoinScope
import com.markettwits.start.register.di.startRegistrationModule
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.component.RegistrationMemberComponentBase
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import kotlinx.coroutines.flow.MutableStateFlow

class StartDistanceComponentBase(
    componentContext: ComponentContext,
    private val innerState : StartRegistrationStagePage.Registration,
    private val onMessage: (EventContent) -> Unit,
    private val onGoBack : (StartRegistrationStagePage) -> Unit,
    private val onGoNext : (StartRegistrationStagePage) -> Unit
) : StartDistanceComponent, ComponentContext by componentContext {

    private val feature = instanceKeeper.getOrCreate {
        StartDistanceFeature(
            innerState = innerState,
            onMessage = onMessage
        )
    }

    private val scope = getOrCreateKoinScope(
        listOf(startRegistrationModule)
    )

    override val state: MutableStateFlow<StartRegistrationStagePage.Registration> = feature.state

    private val slotNavigation = SlotNavigation<StartDistanceComponent.Config>()

    override val childSlot: Value<ChildSlot<*, StartDistanceComponent.Child>> = childSlot(
        serializer = StartDistanceComponent.Config.serializer(),
        source = slotNavigation,
        handleBackButton = true,
        childFactory = ::childSlot)

    override fun onClickStartMember(startStatement: StartStatement) {
        slotNavigation.activate(
            StartDistanceComponent.Config.StartRegistrationMember(
                1,
                startStatement.members,
                startStatement
            )
        )
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


    private fun childSlot(
        config: StartDistanceComponent.Config,
        componentContext: ComponentContext
    ): StartDistanceComponent.Child = when (config) {
        is StartDistanceComponent.Config.StartRegistrationMember -> StartDistanceComponent.Child.StartRegistrationMember(
            RegistrationMemberComponentBase(
                componentContext = componentContext,
                startStatement = config.startStatement,
                memberId = config.memberId,
                storeFactory = scope.get(),
                membersProfile = config.profileMembers,
                apply = { member, id ->
                    feature.onChangeStartStatement(
                        startStatement = member,
                    )
                    slotNavigation.dismiss()
                },
                pop = { slotNavigation.dismiss() },
            )
        )
    }
}