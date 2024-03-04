package com.markettwits.profile.presentation.deprecated

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.edit_profile.edit_profile.presentation.EditProfileScreen
import com.markettwits.edit_profile.edit_social_network.presentation.components.ProfileSocialNetworkScreen
import com.markettwits.edit_profile.root.RootEditProfileScreen
import com.markettwits.members.member_root.component.RootMembersScreen
import com.markettwits.profile.api.root.RootAuthFlowScreen
import com.markettwits.profile.presentation.component.authorized.presentation.components.NewAuthorizedProfileScreen
import com.markettwits.profile.presentation.component.unauthorized.components.NewUnAuthorizedProfileScreen
import com.markettwits.registrations.registrations_list.presentation.MyRegistrationsScreen
import com.markettwits.registrations.root_registrations.RootRegistrationsScreen
import com.markettwits.registrations.start_order_detail.components.StartOrderProfileDialogScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun DefaultProfileScreen(component: DefaultProfileComponent) {

    val childStack by component.childStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also { child ->
        when (child) {
            is DefaultProfileComponent.SlotChild.StartOrder -> StartOrderProfileDialogScreen(
                component = child.component
            )
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is DefaultProfileComponent.Child.AuthFlow -> RootAuthFlowScreen(component = child.component)
            is DefaultProfileComponent.Child.AuthProfile -> NewAuthorizedProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.UnAuthProfile -> NewUnAuthorizedProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.EditProfile -> EditProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.MyRegistries -> RootRegistrationsScreen(component = child.component)
            is DefaultProfileComponent.Child.EditProfileMenu -> RootEditProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.SocialNetwork -> ProfileSocialNetworkScreen(component = child.component)
            is DefaultProfileComponent.Child.Start -> RootStartScreen(component = child.component)
            is DefaultProfileComponent.Child.UserStarts -> MyRegistrationsScreen(component = child.component)
            is DefaultProfileComponent.Child.Members -> RootMembersScreen(component = child.component)
        }
    }
}