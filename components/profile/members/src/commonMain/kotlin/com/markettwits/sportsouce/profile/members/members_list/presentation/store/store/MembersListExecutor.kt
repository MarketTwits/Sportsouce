package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.isNetworkConnectionError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.members_list.domain.MembersListUseCase
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MembersListExecutor(
    private val useCase: MembersListUseCase,
    private val exceptionTracker: ExceptionTracker,
) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            is Intent.GoBack -> publish(Label.GoBack)
            is Intent.OnClickAddMember -> publish(Label.OnClickAddMember)
            is Intent.OnClickMember -> publish(Label.OnClickMember(intent.member))
            is Intent.Retry -> launch(true)
            is Intent.UpdateMember -> updateMember(intent.member, state().members)
        }
    }

    override fun executeAction(action: Unit) {
        launch(false)
    }

    private fun launch(forced: Boolean) {
        scope.launch {
            dispatch(Message.Loading)
            useCase.members(forced)
                .catch {
                    dispatch(Message.Error(it.mapToSauceError()))
                    if (!it.isNetworkConnectionError())
                        exceptionTracker.reportException(it, "MembersListExecutor#launch")
                }
                .collect { members ->
                    dispatch(Message.Loaded(members))
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
