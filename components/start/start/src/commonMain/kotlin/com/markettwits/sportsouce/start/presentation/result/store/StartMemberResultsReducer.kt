package com.markettwits.sportsouce.start.presentation.result.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.Message
import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore.State

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