package com.markettwits.start.presentation.member.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.start.domain.StartStatement
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Intent
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.Label
import com.markettwits.start.presentation.member.store.RegistrationMemberStore.State

interface RegistrationMemberStore : Store<Intent, State, Label> {
    data class State(
        val value: StartStatement,
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
