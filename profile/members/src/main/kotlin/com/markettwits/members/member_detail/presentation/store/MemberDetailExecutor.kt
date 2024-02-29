package com.markettwits.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.members.member_detail.domain.MemberDetailUseCase
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Intent
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Label
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Message
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.State
import kotlinx.coroutines.launch

class MemberDetailExecutor(private val useCase: MemberDetailUseCase) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickDelete -> deleteMember(getState().member.id)
            is Intent.OnClickEdit -> publish(Label.OnClickEdit)
        }
    }

    private fun deleteMember(memberId: Int) {
        scope.launch {
            dispatch(Message.DeleteLoading)
            useCase.deleteMember(memberId).fold(
                onSuccess = {
                    publish(Label.MemberDeleted)
                }, onFailure = {
                    dispatch(Message.DeleteFailure(it.message.toString()))
                })
        }
    }
}
