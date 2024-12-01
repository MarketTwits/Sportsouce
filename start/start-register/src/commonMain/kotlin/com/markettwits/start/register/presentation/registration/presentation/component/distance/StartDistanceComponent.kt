package com.markettwits.start.register.presentation.registration.presentation.component.distance

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.component.RegistrationMemberComponent
import com.markettwits.start.register.presentation.registration.domain.models.StartRegistrationStatementAnswer
import com.markettwits.start.register.presentation.registration.presentation.component.StartStageComponent
import com.markettwits.start.register.presentation.registration.presentation.components.registration.StartRegistrationStagePage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface StartDistanceComponent : StartStageComponent {

    override val state: StateFlow<StartRegistrationStagePage.Registration>

    val childSlot: Value<ChildSlot<*, Child>>

    fun onClickStartMember(startStatement: StartStatement)

    fun onChangeStartStatement(startStatement: StartStatement)

    fun onChangeAnswer(startRegisterAnswer: StartRegistrationStatementAnswer)

    fun onClickGoBack()

    fun onClickGoNext()

    @Serializable
    sealed interface Config {

        @Serializable
        data class StartRegistrationMember(
            val memberId: Int,
            val profileMembers: List<ProfileMember>,
            val startStatement: StartStatement,
        ) : Config
    }

    sealed class Child {
        data class StartRegistrationMember(val component: RegistrationMemberComponent) : Child()
    }

}