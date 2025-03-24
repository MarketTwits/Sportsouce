package com.markettwits.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Intent
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.Label
import com.markettwits.members.members_list.presentation.store.store.MembersListStore.State

interface MembersListStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = "",
        val members: List<ProfileMember> = emptyList(),
    )

    sealed interface Intent {
        data object Retry : Intent
        data object GoBack : Intent
        data object OnClickAddMember : Intent
        data class UpdateMember(val member: ProfileMember) : Intent
        data class OnClickMember(val member: ProfileMember) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Error(val message: String) : Message
        data class Loaded(val members: List<ProfileMember>) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data object OnClickAddMember : Label
        data class OnClickMember(val member: ProfileMember) : Label
    }

}
