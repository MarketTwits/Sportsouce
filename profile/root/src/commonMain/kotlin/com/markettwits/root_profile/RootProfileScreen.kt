package com.markettwits.root_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.base_screen.AdaptivePane
import com.markettwits.profile.api.root.RootAuthFlowScreen
import com.markettwits.profile.root.RootAuthorizedProfileScreen
import com.markettwits.unauthorized.presentation.screen.UnAuthorizedProfileScreen

@Composable
fun RootProfileScreen(component: RootProfileComponent) {
    val childStack by component.childStack.subscribeAsState()
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade())
    ) {
        when (val child = it.instance) {
            is RootProfileComponent.Child.AuthProfile -> RootAuthorizedProfileScreen(component = child.component)
            is RootProfileComponent.Child.UnAuthProfile -> UnAuthorizedProfileScreen(component = child.component)
            is RootProfileComponent.Child.AuthFlow -> RootAuthFlowScreen(component = child.component)
        }
    }
}