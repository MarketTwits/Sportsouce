package com.markettwits.sportsouce.start.register.presentation.registration.member.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.start.register.domain.StartStatement
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore.*

interface RegistrationMemberStore : Store<Intent, State, Label> {
    data class State(
        val userNumber: Int,
        val value: StartStatement,
        val members: List<ProfileMember>,
        val isSuggestAddDialogVisible: Boolean = false,
        val isAddMemberDialogVisible: Boolean = false,
        val addMemberRelationType: String = "Партнер по команде",
        val addMemberEmail: String = "",
        val addMemberPhone: String = "",
        val isAddMemberValidationVisible: Boolean = false,
        val addMemberDialogErrorMessage: String? = null,
        val isPendingContinueAfterAdd: Boolean = false,
        val isAddMemberLoading: Boolean = false,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Intent {
        data object OnClickContinue : Intent
        data object OnClickAddToProfile : Intent
        data object OnDismissSuggestAddDialog : Intent
        data object OnConfirmSuggestAddDialog : Intent
        data object OnContinueWithoutAdd : Intent
        data object OnDismissAddMemberDialog : Intent
        data class OnChangeAddMemberRelationType(val value: String) : Intent
        data class OnChangeAddMemberEmail(val value: String) : Intent
        data class OnChangeAddMemberPhone(val value: String) : Intent
        data object OnConfirmAddMemberToProfile : Intent
        data object OnConsumedEvent : Intent
        data object Pop : Intent
        data class ChangeFiled(val startStatement: StartStatement) : Intent
    }

    sealed interface Message {
        data class OnValueChanged(val startStatement: StartStatement) : Message
        data class OnMembersChanged(val members: List<ProfileMember>) : Message
        data class ChangeSuggestAddDialogState(val show: Boolean) : Message
        data class ChangeAddMemberDialogState(val show: Boolean) : Message
        data class ChangeAddMemberRelationType(val value: String) : Message
        data class ChangeAddMemberEmail(val value: String) : Message
        data class ChangeAddMemberPhone(val value: String) : Message
        data class ChangeAddMemberValidationVisible(val value: Boolean) : Message
        data class ChangeAddMemberDialogErrorMessage(val value: String?) : Message
        data class ChangePendingContinueAfterAdd(val value: Boolean) : Message
        data class ChangeAddMemberLoading(val value: Boolean) : Message
        data class ShowEvent(val message: String, val success: Boolean = false) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data class OnClickContinue(val startStatement: StartStatement) : Label
        data object OnClickPop : Label
    }

}
