package com.markettwits.profile.presentation.deprecated

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.change_password.presentation.screen.ChangePasswordScreen
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.edit_profile.edit_profile.presentation.EditProfileScreen
import com.markettwits.edit_profile.root.RootEditProfileScreen
import com.markettwits.edit_profile.social_network.presentation.components.ProfileSocialNetworkScreen
import com.markettwits.profile.presentation.component.authorized.presentation.components.NewAuthorizedProfileScreen
import com.markettwits.profile.presentation.component.my_members.MyMembersScreen
import com.markettwits.profile.presentation.component.unauthorized.components.NewUnAuthorizedProfileScreen
import com.markettwits.profile.presentation.sign_in.AuthScreen
import com.markettwits.profile.presentation.sign_up.presentation.content.SignUpScreen
import com.markettwits.registrations.root_registrations.RootRegistrationsScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun DefaultProfileScreen(component: DefaultProfileComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DefaultProfileComponent.Child.Login -> AuthScreen(component = child.component)
            is DefaultProfileComponent.Child.AuthProfile -> SportSouceTheme {
                NewAuthorizedProfileScreen(component = child.component)
            }
            is DefaultProfileComponent.Child.UnAuthProfile -> NewUnAuthorizedProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.EditProfile -> EditProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.MyRegistries -> RootRegistrationsScreen(component = child.component)
            is DefaultProfileComponent.Child.MyMembers -> MyMembersScreen(component = child.component)
            is DefaultProfileComponent.Child.ChangePassword -> ChangePasswordScreen(component = child.component)
            is DefaultProfileComponent.Child.SignUp -> SignUpScreen(component = child.component)
            is DefaultProfileComponent.Child.EditProfileMenu -> RootEditProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.SocialNetwork -> ProfileSocialNetworkScreen(component = child.component)
            is DefaultProfileComponent.Child.Start -> RootStartScreen(component = child.component)
        }
    }
}