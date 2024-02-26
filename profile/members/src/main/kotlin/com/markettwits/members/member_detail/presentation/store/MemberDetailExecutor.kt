package com.markettwits.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Intent
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Label
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Message
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.State

class MemberDetailExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickDelete -> TODO()
            is Intent.OnClickEdit -> TODO()
        }
    }

}
