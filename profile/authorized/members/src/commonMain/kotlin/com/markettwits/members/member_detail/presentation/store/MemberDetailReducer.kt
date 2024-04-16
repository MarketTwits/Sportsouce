package com.markettwits.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Message
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.State

object MemberDetailReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.DeleteFailure -> copy(
                isLoading = false,
                isError = true,
                message = msg.message
            )

            is Message.DeleteLoading -> copy(isLoading = true, isError = false)
            is Message.DeleteSuccess -> copy(isLoading = false, isError = false)
        }
    }
}