package com.markettwits.edit_profile.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.change_password.presentation.screen.ChangePasswordScreen
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.edit_profile.edit_menu.presentation.EditProfileScreen
import com.markettwits.edit_profile.edit_profile_Image.presentation.components.EditProfileImageScreen
import com.markettwits.edit_profile.edit_profile_about.presentation.components.EditProfileAboutScreen
import com.markettwits.edit_profile.edit_profile_info.presentation.components.EditProfileInfoScreen
import com.markettwits.edit_profile.social_network.presentation.components.ProfileSocialNetworkScreen

@Composable
fun RootEditProfileScreen(component: RootEditProfileComponent) {

    val childSlot by component.childSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    childSlot.child?.instance?.also {
        when (it) {
            is RootEditProfileComponent.ChildSlot.EditProfileAbout -> SportSouceTheme {
                EditProfileAboutScreen(
                    component = it.component
                )
            }

            is RootEditProfileComponent.ChildSlot.EditProfileImage ->
                SportSouceTheme {
                    EditProfileImageScreen(
                        component = it.component
                    )
                }
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
            is RootEditProfileComponent.ChildStack.ChangePassword -> SportSouceTheme {
                ChangePasswordScreen(
                    component = child.component
                )
            }

            is RootEditProfileComponent.ChildStack.EditProfileInfo -> EditProfileInfoScreen(
                component = child.component
            )
        }
    }
}