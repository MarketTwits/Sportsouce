package com.markettwits.sportsouce.profile.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.profile.members.member_detail.domain.MemberDetailUseCase
import com.markettwits.sportsouce.profile.members.member_detail.presentation.store.MemberDetailStore.*
import kotlinx.coroutines.launch

class MemberDetailExecutor(private val useCase: MemberDetailUseCase) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnClickDelete -> if (!state().isLoading && state().member.id > 0) deleteMember(state().member.id)
            is Intent.OnClickEdit -> if (!state().isLoading) publish(Label.OnClickEdit)
        }
    }

    private fun deleteMember(memberId: Int) {
        scope.launch {
            dispatch(Message.DeleteLoading)
            useCase.deleteMember(memberId).fold(
                onSuccess = {
                    dispatch(Message.DeleteSuccess)
                    publish(Label.MemberDeleted)
                }, onFailure = {
                    dispatch(Message.DeleteFailure(it.mapToSauceError().mapToString()))
                })
        }
    }
}
