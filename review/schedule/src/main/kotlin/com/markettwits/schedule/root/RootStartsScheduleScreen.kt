package com.markettwits.schedule.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.schedule.schedule.presentation.screen.StartsScheduleScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun RootStartsScheduleScreen(component : RootStartsScheduleComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsScheduleComponent.Child.Schedule -> StartsScheduleScreen(component = child.component)
            is RootStartsScheduleComponent.Child.Start -> RootStartScreen(child.component)
        }
    }
}