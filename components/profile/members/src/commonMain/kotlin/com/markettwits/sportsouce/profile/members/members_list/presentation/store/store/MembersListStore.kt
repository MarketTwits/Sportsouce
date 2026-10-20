package com.markettwits.sportsouce.profile.members.members_list.presentation.store.store

import com.markettwits.sportsouce.profile.members.member_common.domain.MemberIdentityTeam
import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore.*

interface MembersListStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val team: MemberIdentityTeam? = null,
        val teamLoading: Boolean = true,
        val teamError: String? = null,
        val actingTeamKey: String? = null,
        val trustedChangesAwaitingRefresh: Set<Int> = emptySet(),
        val error: SauceError? = null,
        val isSuccess: Boolean = false,
        val members: List<ProfileMember> = emptyList(),
    )

    sealed interface Intent {
        data object Retry : Intent
        data class RespondToRequest(val id: Int, val accept: Boolean) : Intent
        data class UpdateRelation(val userId: Int, val relation: String?) : Intent
        data class UpdateAutoAccept(val trustId: Int, val enabled: Boolean) : Intent
        data class TrustPerson(val userId: Int) : Intent
        data object GoBack : Intent
        data object OnClickAddMember : Intent
        data object OnClickSearchMember : Intent
        data class UpdateMember(val member: ProfileMember) : Intent
        data class RemoveMember(val id: Int) : Intent
        data class OnClickMember(val member: ProfileMember) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data object TeamLoading : Message
        data class TeamActionLoading(val key: String) : Message
        data object TeamActionDone : Message
        data class TrustedPersonAdded(val userId: Int) : Message
        data class TeamLoaded(val team: MemberIdentityTeam) : Message
        data class TeamFailed(val message: String) : Message
        data class Error(val error: SauceError) : Message
        data class Loaded(val members: List<ProfileMember>) : Message
    }

    sealed interface Label {
        data object GoBack : Label
        data object OnClickAddMember : Label
        data object OnClickSearchMember : Label
        data class OnClickMember(val member: ProfileMember) : Label
    }

}
