package com.markettwits.sportsouce.start.register.presentation.registration.member.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore.Message
import com.markettwits.sportsouce.start.register.presentation.registration.member.store.RegistrationMemberStore.State

object RegistrationMemberReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.OnValueChanged -> copy(value = msg.startStatement)
            is Message.OnMembersChanged -> copy(members = msg.members)
            is Message.ChangeSuggestAddDialogState -> copy(isSuggestAddDialogVisible = msg.show)
            is Message.ChangeAddMemberDialogState -> copy(isAddMemberDialogVisible = msg.show)
            is Message.ChangeAddMemberRelationType -> copy(addMemberRelationType = msg.value)
            is Message.ChangeAddMemberEmail -> copy(addMemberEmail = msg.value)
            is Message.ChangeAddMemberPhone -> copy(addMemberPhone = msg.value)
            is Message.ChangeAddMemberValidationVisible -> copy(isAddMemberValidationVisible = msg.value)
            is Message.ChangeAddMemberDialogErrorMessage -> copy(addMemberDialogErrorMessage = msg.value)
            is Message.ChangePendingContinueAfterAdd -> copy(isPendingContinueAfterAdd = msg.value)
            is Message.ChangeAddMemberLoading -> copy(isAddMemberLoading = msg.value)
            is Message.ShowEvent -> copy(event = triggered(EventContent(msg.success, msg.message)))
            is Message.OnConsumedEvent -> copy(event = consumed())
        }
    }
}
