package com.markettwits.start.register.presentation.member.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.start.register.domain.StartStatement
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.Intent
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.Label
import com.markettwits.start.register.presentation.member.store.RegistrationMemberStore.State

interface RegistrationMemberStore : Store<Intent, State, Label> {
    data class State(
        val userNumber: Int,
        val value: StartStatement,
        val members: List<ProfileMember>,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Intent {
        data object OnClickContinue : Intent
        data object OnConsumedEvent : Intent
        data object Pop : Intent
        data class ChangeFiled(val startStatement: StartStatement) : Intent
    }

    sealed interface Message {
        data class OnValueChanged(val startStatement: StartStatement) : Message
        data class ShowEvent(val message: String) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data class OnClickContinue(val startStatement: StartStatement) : Label
        data object OnClickPop : Label
    }

}
