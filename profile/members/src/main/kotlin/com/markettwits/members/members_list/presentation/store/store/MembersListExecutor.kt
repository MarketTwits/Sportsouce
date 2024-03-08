package com.markettwits.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.members_list.domain.MembersListUseCase
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Intent
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Label
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Message
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MembersListExecutor(private val useCase: MembersListUseCase) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickAddMember -> publish(Label.OnClickAddMember)
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member))
            is Intent.Retry -> launch()
            is Intent.UpdateMember -> updateMember(intent.member, getState().members)
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            useCase.members(false)
                .catch {
                    dispatch(Message.Error(it.message.toString()))
                }
                .collect {
                    dispatch(Message.Loaded(it))
                }
        }
    }

    private fun updateMember(member: ProfileMember, currentList: List<ProfileMember>) {
        val index = currentList.indexOfFirst { it.id == member.id }
        if (index >= 0) {
            val newList = currentList.toMutableList()
            newList[index] = member
            dispatch(Message.Loaded(newList))
        }
    }
}
