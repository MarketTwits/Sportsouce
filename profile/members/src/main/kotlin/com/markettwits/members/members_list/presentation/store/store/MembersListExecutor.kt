package com.markettwits.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.members.members_list.domain.MembersListUseCase
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Intent
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Label
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Message
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.State
import kotlinx.coroutines.launch

class MembersListExecutor(private val useCase: MembersListUseCase) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickAddMember -> publish(Label.OnClickAddMember)
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member))
            is Intent.Retry -> launch()
        }
    }

    override fun executeAction(action: Unit, getState: () -> State) {
        launch()
    }

    private fun launch() {
        scope.launch {
            dispatch(Message.Loading)
            useCase.members(false).fold(
                onSuccess = {
                    dispatch(Message.Loaded(it))
                },
                onFailure = {
                    dispatch(Message.Error(it.message.toString()))
                }
            )
        }
    }
}
