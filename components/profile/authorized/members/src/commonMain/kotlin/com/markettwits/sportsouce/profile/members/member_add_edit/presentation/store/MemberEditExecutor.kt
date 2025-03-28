package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.add.MemberAddUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.edit.MemberEditUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.Intent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.Label
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.Message
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.State
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import kotlinx.coroutines.launch

class MemberEditExecutor(
    private val editUseCase: MemberEditUseCase,
    private val addUseCase: MemberAddUseCase,
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnValueChanged -> dispatch(Message.OnValueChanged(intent.member))
            is Intent.Save -> execute(state().member, state().mode)
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
            is Intent.Retry -> launch()
        }
    }

    override fun executeAction(action: Unit) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            editUseCase.teams().fold(
                onSuccess = {
                    dispatch(Message.Loaded(it))
                },
                onFailure = {
                    dispatch(Message.Error(it.networkExceptionHandler().message.toString()))
                }
            )
        }
    }

    private fun execute(profileMember: ProfileMember, mode: MemberEditComponent.Mode) {
        if (mode is MemberEditComponent.Mode.Edit) {
            editMember(profileMember)
        } else {
            addMember(profileMember)
        }
    }

    private fun editMember(profileMember: ProfileMember) {
        scope.launch {
            dispatch(Message.Loading)
            editUseCase.edit(profileMember).fold(
                onSuccess = {
                    publish(Label.UpdateSuccess(profileMember))
                    dispatch(Message.UpdateSuccess("Участник успешно обновлен"))
                },
                onFailure = {
                    dispatch(Message.Error(it.networkExceptionHandler().message.toString()))
                }
            )
        }
    }

    private fun addMember(profileMember: ProfileMember) {
        scope.launch {
            dispatch(Message.Loading)
            addUseCase.addMember(profileMember).fold(onSuccess = {
                publish(Label.UpdateSuccess(profileMember))
                dispatch(Message.UpdateSuccess("Участник добавлен"))
            }, onFailure = {
                dispatch(Message.Error(it.networkExceptionHandler().message.toString()))
            })
        }
    }

}
