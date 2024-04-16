package com.markettwits.members.member_add_edit.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.members.member_add_edit.presentation.store.MemberEditStore.Message
import com.markettwits.members.member_add_edit.presentation.store.MemberEditStore.State

object MemberEditReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.OnValueChanged -> copy(member = msg.member)
            is Message.Error -> copy(
                isLoading = false,
                isError = true,
                message = msg.message,
                event = triggered(EventContent(false, msg.message))
            )

            is Message.Loaded -> copy(isLoading = false, teams = msg.teams)
            is Message.Loading -> copy(isLoading = true, isError = false)
            is Message.UpdateSuccess -> copy(isLoading = false, message = msg.message)
            is Message.OnConsumedEvent -> copy(event = consumed())
        }
    }
}