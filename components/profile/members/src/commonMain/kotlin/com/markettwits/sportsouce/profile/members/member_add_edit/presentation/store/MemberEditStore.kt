package com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.Intent
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.Label
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.store.MemberEditStore.State
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.member_common.domain.emptyProfileMember
import com.markettwits.sportsouce.teams_city.domain.Team

interface MemberEditStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val member: ProfileMember = emptyProfileMember,
        val mode: MemberEditComponent.Mode,
        val teams: List<Team> = emptyList(),
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Intent {
        data object OnConsumedEvent : Intent
        data object Dismiss : Intent
        data class OnValueChanged(val member: ProfileMember) : Intent
        data object Save : Intent
        data object Retry : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class Error(val message: String) : Message
        data class Loaded(val teams: List<Team>) : Message
        data class UpdateSuccess(val message: String) : Message
        data class OnValueChanged(val member: ProfileMember) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data object Dismiss : Label
        data class UpdateSuccess(val member: ProfileMember) : Label
    }

}
