package com.markettwits.members.member_edit.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.member_common.domain.emptyProfileMember
import com.markettwits.members.member_edit.presentation.component.MemberEditComponent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Intent
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.Label
import com.markettwits.members.member_edit.presentation.store.MemberEditStore.State
import com.markettwits.teams_city.domain.Team

interface MemberEditStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val member: ProfileMember = emptyProfileMember,
        val mode: MemberEditComponent.Mode,
        val teams: List<Team> = emptyList()
    )

    sealed interface Intent {
        data object Dismiss : Intent
        data class OnValueChanged(val member: ProfileMember) : Intent
        data object Save : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Error(val message: String) : Message
        data class Loaded(val teams: List<Team>) : Message
        data class UpdateSuccess(val message: String) : Message
        data class OnValueChanged(val member: ProfileMember) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class UpdateSuccess(val member: ProfileMember) : Label
    }

}
