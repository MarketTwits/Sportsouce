package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.errors.api.throwable.mapToString
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.add.MemberAddUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.domain.edit.MemberEditUseCase
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.*
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive

class MemberEditExecutor(
    private val editUseCase: MemberEditUseCase,
    private val addUseCase: MemberAddUseCase,
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    private var teamsJob: Job? = null

    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
            is Intent.OnValueChanged -> if (!state().isLoading && !state().isSaving) dispatch(Message.OnValueChanged(intent.member))
            is Intent.Save -> if (!state().isLoading && !state().isSaving && state().loadError == null) execute(state().member, state().mode)
            is Intent.OnConsumedEvent -> dispatch(Message.OnConsumedEvent)
            is Intent.RetryTeams -> if (!state().isSaving) loadTeams()
            is Intent.Retry -> if (!state().isLoading && !state().isSaving) launch()
        }
    }

    override fun executeAction(action: Unit) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            val member = if (state().mode is MemberEditComponent.Mode.Edit) {
                editUseCase.member(state().member.id).getOrElse {
                    dispatch(Message.LoadError(it.mapToSauceError().mapToString()))
                    return@launch
                }
            } else null
            dispatch(Message.Loaded(state().teams, member))
            loadTeams()
        }
    }

    private fun loadTeams() {
        teamsJob?.cancel()
        dispatch(Message.TeamsLoading)
        teamsJob = scope.launch {
            val result = editUseCase.teams()
            ensureActive()
            result.fold(onSuccess = { dispatch(Message.TeamsLoaded(it)) }, onFailure = { dispatch(Message.TeamsFailed) })
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
            dispatch(Message.Saving)
            editUseCase.edit(profileMember).fold(
                onSuccess = {
                    val fresh = editUseCase.member(profileMember.id).getOrDefault(profileMember.copy(
                        phone = if (profileMember.child) "" else profileMember.phone,
                        email = if (profileMember.child) "" else profileMember.email.trim()))
                    publish(Label.UpdateSuccess(fresh))
                    dispatch(Message.UpdateSuccess("Участник успешно обновлен"))
                },
                onFailure = {
                    dispatch(Message.Error(it.mapToSauceError().mapToString()))
                }
            )
        }
    }

    private fun addMember(profileMember: ProfileMember) {
        scope.launch {
            dispatch(Message.Saving)
            addUseCase.addMember(profileMember).fold(onSuccess = {
                publish(Label.UpdateSuccess(profileMember))
                dispatch(Message.UpdateSuccess("Участник добавлен"))
            }, onFailure = {
                dispatch(Message.Error(it.mapToSauceError().mapToString()))
            })
        }
    }

}
