package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.*

interface MembersListStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val error: SauceError? = null,
        val isSuccess: Boolean = false,
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
        data class Error(val error: SauceError) : Message
        data class Loaded(val members: List<ProfileMember>) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data object OnClickAddMember : Label
        data class OnClickMember(val member: ProfileMember) : Label
    }

}
