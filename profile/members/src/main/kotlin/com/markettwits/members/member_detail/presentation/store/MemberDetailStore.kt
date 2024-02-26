package com.markettwits.members.member_detail.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.members.common.domain.ProfileMember
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Intent
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.Label
import com.markettwits.members.member_detail.presentation.store.MemberDetailStore.State

interface MemberDetailStore : Store<Intent, State, Label> {
    data class State(val member: ProfileMember)

    sealed interface Intent {
        data object Dismiss : Intent
        data object OnClickEdit : Intent
        data object OnClickDelete : Intent
    }

    sealed interface Message

    sealed interface Label {
        data object Dismiss : Label
        data object OnClickEdit : Label
    }

}
