package com.markettwits.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Message
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.State

object MembersListReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Error -> copy(isLoading = false, isError = true, message = msg.message)
            is Message.Loaded -> copy(
                isLoading = false,
                isError = false,
                isSuccess = true,
                members = msg.members
            )

            is Message.Loading -> copy(isLoading = true)
        }
    }
}