package com.markettwits.members.member_edit.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.members.common.domain.ProfileMember
import com.markettwits.members.member_add.domain.MemberAddUseCase
import com.markettwits.members.member_edit.domain.MemberEditUseCase
import com.markettwits.members.member_edit.presentation.component.MemberEditComponent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Intent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Label
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.State
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Message
import kotlinx.coroutines.launch

class MemberEditExecutor(
    private val editUseCase: MemberEditUseCase,
    private val addUseCase: MemberAddUseCase,
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnValueChanged -> dispatch(Message.OnValueChanged(intent.member))
            is Intent.Save -> execute(getState().member, getState().mode)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
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
                    dispatch(Message.Error(it.message.toString()))
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
                    dispatch(Message.Error(it.message.toString()))
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
                dispatch(Message.Error(it.message.toString()))
            })
        }

    }

}
