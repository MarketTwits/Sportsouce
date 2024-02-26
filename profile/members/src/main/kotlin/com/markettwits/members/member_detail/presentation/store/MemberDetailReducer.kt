package com.markettwits.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Message
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.State

object MemberDetailReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}