package com.markettwits.sportsouce.profile.members.member_root.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.profile.members.member_add_edit.presentation.components.MemberEditScreenDialog
import com.markettwits.sportsouce.profile.members.member_detail.presentation.components.MemberDetailScreenDialog
import com.markettwits.sportsouce.profile.members.members_list.presentation.components.MembersScreen

@Composable
fun RootMembersScreen(component: RootMembersComponent) {

    val childStack by component.childStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also { child ->
        when (child) {
            is RootMembersComponent.ChildSlot.MemberDetail -> MemberDetailScreenDialog(component = child.component)
            is RootMembersComponent.ChildSlot.MemberEdit -> MemberEditScreenDialog(component = child.component)
            RootMembersComponent.ChildSlot.MemberIdle -> {}
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootMembersComponent.ChildStack.MembersList -> MembersScreen(component = child.component)
        }
    }
}