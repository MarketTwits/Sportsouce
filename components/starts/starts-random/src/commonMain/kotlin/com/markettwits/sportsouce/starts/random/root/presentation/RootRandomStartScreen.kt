package com.markettwits.sportsouce.starts.random.root.presentation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.sportsouce.start.root.RootStartScreen
import com.markettwits.sportsouce.starts.random.presentation.StartRandomScreen

@Composable
fun RootRandomStartScreen(component : RootStartRandomComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartRandomComponent.Child.Random -> StartRandomScreen(component = child.component)
            is RootStartRandomComponent.Child.Start -> RootStartScreen(child.component)
        }
    }
}