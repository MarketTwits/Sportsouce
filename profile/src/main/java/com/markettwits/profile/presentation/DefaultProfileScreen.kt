package com.markettwits.profile.presentation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.profile.presentation.component.authorized.AuthorizedProfileScreen
import com.markettwits.profile.presentation.component.change_password.ChangePasswordScreen
import com.markettwits.profile.presentation.component.edit_profile.presentation.EditProfileScreen
import com.markettwits.profile.presentation.component.my_members.MyMembersScreen
import com.markettwits.profile.presentation.component.my_registries.MyRegistriesScreen
import com.markettwits.profile.presentation.component.unauthorized.UnAuthorizedProfileScreen
import com.markettwits.profile.presentation.sign_in.AuthScreen

@Composable
fun DefaultProfileScreen(component: DefaultProfileComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DefaultProfileComponent.Child.Login -> AuthScreen(component = child.component)
            is DefaultProfileComponent.Child.AuthProfile -> AuthorizedProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.UnAuthProfile -> UnAuthorizedProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.EditProfile -> EditProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.MyRegistries -> MyRegistriesScreen(component = child.component)
            is DefaultProfileComponent.Child.MyMembers -> MyMembersScreen(component = child.component)
            is DefaultProfileComponent.Child.ChangePassword -> ChangePasswordScreen(component = child.component)
        }
    }

}