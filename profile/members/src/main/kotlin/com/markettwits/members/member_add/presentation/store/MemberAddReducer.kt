package com.markettwits.members.member_add.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.members.member_add.presentation.store.MemberAddStore.State
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Message

object MemberAddReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            else -> TODO()
        }
    }
}