package com.markettwits.profile.presentation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.profile.presentation.screens.ProfileScreen
import com.markettwits.profile.presentation.sign_in.AuthScreen
import com.markettwits.profile.presentation.sign_in.SignInScreen

@Composable
fun DefaultProfileScreen(component: DefaultProfileComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DefaultProfileComponent.Child.Profile -> ProfileScreen(component = child.component)
            is DefaultProfileComponent.Child.Login -> AuthScreen(component = child.component)
        }
    }

}