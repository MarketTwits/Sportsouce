package com.markettwits.start.presentation.result.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.State
import com.markettwits.start.presentation.result.store.StartMemberResultsStore.Message

object StartMemberResultsReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Loaded -> copy(
                defaultMembersResult = msg.membersResult,
                visibleMembersResult = msg.membersResult
            )
            is Message.OnChangeQuery -> copy(textQuery = msg.query, visibleMembersResult = msg.membersResult)
        }
    }
}