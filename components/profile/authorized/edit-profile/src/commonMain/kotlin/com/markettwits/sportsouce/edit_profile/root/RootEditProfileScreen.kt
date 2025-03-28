package com.markettwits.sportsouce.edit_profile.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.edit_profile.edit_menu.presentation.EditProfileScreen
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.screen.NewEditProfileImageScreen
import com.markettwits.sportsouce.edit_profile.edit_profile_about.presentation.components.EditProfileAboutScreen
import com.markettwits.sportsouce.edit_profile.edit_profile_change_password.presentation.screen.ChangePasswordScreen
import com.markettwits.sportsouce.edit_profile.edit_profile_info.presentation.components.EditProfileInfoScreen
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.components.EditProfileSignOutScreenDialog
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.screen.ProfileSocialNetworkScreen

@Composable
fun RootEditProfileScreen(component: RootEditProfileComponent) {

    val childSlot by component.childSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    childSlot.child?.instance?.also {
        when (it) {
            is RootEditProfileComponent.ChildSlot.EditProfileAbout ->
                EditProfileAboutScreen(
                    component = it.component
                )

            is RootEditProfileComponent.ChildSlot.EditProfileImage ->
                NewEditProfileImageScreen(
                    component = it.component
                )

            is RootEditProfileComponent.ChildSlot.EditProfileSignOut -> EditProfileSignOutScreenDialog(
                component = it.component
            )
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootEditProfileComponent.ChildStack.SocialNetwork -> ProfileSocialNetworkScreen(
                component = child.component
            )
            is RootEditProfileComponent.ChildStack.EditProfileMenu -> EditProfileScreen(component = child.component)
            is RootEditProfileComponent.ChildStack.ChangePassword -> ChangePasswordScreen(component = child.component)
            is RootEditProfileComponent.ChildStack.EditProfileInfo -> EditProfileInfoScreen(
                component = child.component
            )
        }
    }
}