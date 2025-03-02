package com.markettwits.start.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.api.root.RootAuthFlowScreen
import com.markettwits.start.register.presentation.distances.screen.StartDistancesScreen
import com.markettwits.start.register.presentation.registration.registration.components.StartRegistrationPage
import com.markettwits.start.register.presentation.success.RegisterSuccessScreen

@Composable
fun RootStartRegisterScreen(component: RootStartRegister) {

    val childStack by component.childStack.subscribeAsState()

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartRegister.Child.StartRegistrationSuccess -> RegisterSuccessScreen(component = child.component)
            is RootStartRegister.Child.StartRegistrationPage -> StartRegistrationPage(component = child.component)
            is RootStartRegister.Child.AuthFlow -> RootAuthFlowScreen(component = child.component)
            is RootStartRegister.Child.StartRegistrationDistances -> StartDistancesScreen(component = child.component)
        }
    }
}