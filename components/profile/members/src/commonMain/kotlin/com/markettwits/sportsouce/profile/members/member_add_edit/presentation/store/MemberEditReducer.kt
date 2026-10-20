package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.Message
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.State

object MemberEditReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.OnValueChanged -> copy(member = msg.member)
            is Message.Error -> copy(
                isLoading = false,
                isSaving = false,
                isError = true,
                message = msg.message,
                event = triggered(EventContent(false, msg.message))
            )

            is Message.Loaded -> copy(isLoading = false, isError = false, teams = msg.teams, member = msg.member ?: member)
            is Message.TeamsLoading -> copy(teamsLoading = true, teamsError = false)
            is Message.TeamsLoaded -> copy(teams = msg.teams, teamsLoading = false, teamsError = false)
            is Message.TeamsFailed -> copy(teamsLoading = false, teamsError = true)
            is Message.Saving -> copy(isSaving = true, isError = false)
            is Message.Loading -> copy(isLoading = true, isError = false, loadError = null)
            is Message.LoadError -> copy(isLoading = false, loadError = msg.message)
            is Message.UpdateSuccess -> copy(isLoading = false, isSaving = false, message = msg.message)
            is Message.OnConsumedEvent -> copy(event = consumed())
        }
    }
}