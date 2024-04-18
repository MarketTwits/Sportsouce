package com.markettwits.profile.api.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.internal.forgot_password.presentation.components.ForgotPasswordScreen
import com.markettwits.profile.internal.sign_in.presentation.components.SignInScreen
import com.markettwits.profile.internal.sign_up.presentation.components.SignUpScreen

@Composable
fun RootAuthFlowScreen(component: RootAuthFlowComponent) {
    val childStack by component.childStack.subscribeAsState()
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootAuthFlowComponent.Child.ForgotPassword -> ForgotPasswordScreen(component = child.component)
            is RootAuthFlowComponent.Child.SignIn -> SignInScreen(component = child.component)
            is RootAuthFlowComponent.Child.SignUp -> SignUpScreen(component = child.component)
        }
    }
}