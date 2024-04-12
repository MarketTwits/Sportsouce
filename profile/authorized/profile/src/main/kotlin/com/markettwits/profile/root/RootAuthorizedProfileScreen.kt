package com.markettwits.profile.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.edit_profile.edit_social_network.presentation.screen.ProfileSocialNetworkScreen
import com.markettwits.edit_profile.root.RootEditProfileScreen
import com.markettwits.members.member_root.component.RootMembersScreen
import com.markettwits.profile.authorized.presentation.components.AuthorizedProfileScreen
import com.markettwits.registrations.registrations_list.presentation.MyRegistrationsScreen
import com.markettwits.registrations.root_registrations.RootRegistrationsScreen
import com.markettwits.registrations.start_order_detail.components.StartOrderProfileDialogScreen
import com.markettwits.settings.root.RootSettingsScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun RootAuthorizedProfileScreen(component: RootAuthorizedProfileComponent) {

    val childStack by component.childStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also { child ->
        when (child) {
            is RootAuthorizedProfileComponent.SlotChild.StartOrder -> StartOrderProfileDialogScreen(
                component = child.component
            )
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootAuthorizedProfileComponent.Child.AuthProfile -> AuthorizedProfileScreen(
                component = child.component
            )
            is RootAuthorizedProfileComponent.Child.MyRegistries -> RootRegistrationsScreen(
                component = child.component
            )
            is RootAuthorizedProfileComponent.Child.EditProfileMenu -> RootEditProfileScreen(
                component = child.component
            )
            is RootAuthorizedProfileComponent.Child.SocialNetwork -> ProfileSocialNetworkScreen(
                component = child.component
            )
            is RootAuthorizedProfileComponent.Child.Start -> RootStartScreen(component = child.component)
            is RootAuthorizedProfileComponent.Child.UserStarts -> MyRegistrationsScreen(component = child.component)
            is RootAuthorizedProfileComponent.Child.Members -> RootMembersScreen(component = child.component)
            is RootAuthorizedProfileComponent.Child.Settings -> RootSettingsScreen(component = child.component)
        }
    }
}