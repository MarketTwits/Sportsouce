package com.markettwits.members.member_root.component

import com.arkivanov.decompose.value.Value
import com.markettwits.members.member_add_edit.presentation.component.MemberEditComponent
import com.markettwits.members.member_common.domain.ProfileMember
import com.markettwits.members.member_detail.presentation.component.MemberDetailComponent
import com.markettwits.members.members_list.presentation.component.MembersListComponent
import kotlinx.serialization.Serializable

interface RootMembersComponent {

    val childStack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<*, ChildSlot>>

    @Serializable
    sealed interface ChildStack {
        data class MembersList(val component: MembersListComponent) : ChildStack
    }

    @Serializable
    sealed interface ConfigStack {
        @Serializable
        data object MembersList : ConfigStack
    }

    sealed interface ChildSlot {
        @Serializable
        data class MemberDetail(val component: MemberDetailComponent) : ChildSlot
        data class MemberEdit(val component: MemberEditComponent) : ChildSlot
        data object MemberIdle : ChildSlot
    }

    @Serializable
    sealed interface ConfigSlot {
        @Serializable
        data class MemberEdit(val member: ProfileMember?) : ConfigSlot

        @Serializable
        data class MemberDetail(val member: ProfileMember) : ConfigSlot
    }
}