package com.markettwits.sportsouce.start.register.presentation.registration.distance.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.member.component.RegistrationMemberComponent
import com.markettwits.sportsouce.start.register.presentation.registration.common.domain.models.StartRegistrationStatementAnswer
import com.markettwits.sportsouce.start.register.presentation.registration.registration.component.StartStageComponent
import com.markettwits.sportsouce.start.register.presentation.registration.registration.components.StartRegistrationStagePage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface StartDistanceComponent : StartStageComponent {

    val state: StateFlow<StartRegistrationStagePage.Registration>

    val childSlot: Value<ChildSlot<*, Child>>

    fun onClickStartMember(startStatement: StartStatement)

    fun onChangeStartStatement(startStatement: StartStatement)

    fun onChangeDistanceAnswer(startRegisterAnswer: StartRegistrationStatementAnswer)

    fun onChangeStatementAnswer(startRegisterAnswer: StartRegistrationStatementAnswer)

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