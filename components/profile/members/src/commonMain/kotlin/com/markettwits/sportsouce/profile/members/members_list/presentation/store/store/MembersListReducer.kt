package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.Message
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.State

object MembersListReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.TeamLoading -> copy(teamLoading = true, teamError = null)
            is Message.TeamLoaded -> copy(teamLoading = false, team = msg.team, teamError = null,
                trustedChangesAwaitingRefresh = trustedChangesAwaitingRefresh - msg.team.trusted.map { it.userId }.toSet())
            is Message.TeamActionLoading -> copy(actingTeamKey = msg.key, teamError = null)
            is Message.TeamActionDone -> copy(actingTeamKey = null)
            is Message.TrustedPersonAdded -> copy(trustedChangesAwaitingRefresh = trustedChangesAwaitingRefresh + msg.userId)
            is Message.TeamFailed -> copy(teamLoading = false, teamError = msg.message)
            is Message.Error -> copy(isLoading = false, error = msg.error)
            is Message.Loaded -> copy(
                isLoading = false,
                error = null,
                isSuccess = true,
                members = msg.members
            )
            is Message.Loading -> copy(isLoading = true)
        }
    }
}