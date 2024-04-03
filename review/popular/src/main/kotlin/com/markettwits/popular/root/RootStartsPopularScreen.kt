package com.markettwits.popular.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.popular.popular.presentation.screen.PopularStartsScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun RootStartsPopularScreen(component: RootStartsPopularComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsPopularComponent.Child.Popular -> PopularStartsScreen(component = child.component)
            is RootStartsPopularComponent.Child.Start -> RootStartScreen(child.component)
        }
    }
}