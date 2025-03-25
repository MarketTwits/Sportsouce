package com.markettwits.sportsouce.auth.flow.api.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.screen.ForgotPasswordScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.screen.SignInScreen
import com.markettwits.sportsouce.auth.flow.internal.sign_up.presentation.screen.SignUpScreen

@Composable
fun RootAuthFlowScreen(component: RootAuthFlowComponent) {
    val childStack by component.childStack.subscribeAsState()
    AdaptivePane {
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
}