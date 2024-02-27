package com.markettwits.members.member_add.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Intent
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Label
import com.markettwits.members.member_add.presentation.store.MemberAddStore.State
import com.markettwits.members.member_add.presentation.store.MemberAddStore.Message

class MemberAddExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            else -> TODO()
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        super.executeAction(action, getState)
    }
}
