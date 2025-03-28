package com.markettwits.sportsouce.start.register.presentation.member.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.member.store.RegistrationMemberStore.Intent
import com.markettwits.sportsouce.start.register.presentation.member.store.RegistrationMemberStore.Label
import com.markettwits.sportsouce.start.register.presentation.member.store.RegistrationMemberStore.State

interface RegistrationMemberStore : Store<Intent, State, Label> {
    data class State(
        val userNumber: Int,
        val value: StartStatement,
        val members: List<ProfileMember>,
        val isClosedAllerDialog: Boolean,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Intent {
        data object OnClickContinue : Intent
        data object OnClickCloseDialog : Intent
        data object OnConsumedEvent : Intent
        data object Pop : Intent
        data class ChangeFiled(val startStatement: StartStatement) : Intent
    }

    sealed interface Message {
        data class OnValueChanged(val startStatement: StartStatement) : Message
        data class ShowEvent(val message: String) : Message
        data class ChangeAllerDialogState(val show: Boolean) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data class OnClickContinue(val startStatement: StartStatement) : Label
        data object OnClickPop : Label
    }

}
