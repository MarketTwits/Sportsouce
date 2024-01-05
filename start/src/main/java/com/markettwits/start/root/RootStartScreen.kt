package com.markettwits.start.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.start.presentation.membres.filter_screen.screen.StartMembersFilterScreen
import com.markettwits.start.presentation.membres.list.screen.StartMembersScreen
import com.markettwits.start.presentation.start.screen.StartScreen

@Composable
fun RootStartScreen(component: RootStartScreenComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartScreenComponent.Child.Start -> StartScreen(component = child.component)
            is RootStartScreenComponent.Child.StartMembers -> StartMembersScreen(component = child.component)
            is RootStartScreenComponent.Child.StartMembersFilter -> StartMembersFilterScreen(
                component = child.component
            )
        }
    }
}