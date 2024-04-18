package com.markettwits.schedule.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.schedule.schedule.presentation.components.detail.screen.StartDetailScheduleScreen
import com.markettwits.schedule.schedule.presentation.screen.StartsScheduleScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun RootStartsScheduleScreen(component: RootStartsScheduleComponent) {
    val childSlot by component.childSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()


    childSlot.child?.instance?.also { child ->
        when (child) {
            is RootStartsScheduleComponent.ChildSlot.ScheduleDetail -> StartDetailScheduleScreen(
                component = child.component
            )
        }
    }


    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsScheduleComponent.Child.Schedule -> StartsScheduleScreen(component = child.component)
            is RootStartsScheduleComponent.Child.Start -> RootStartScreen(child.component)
        }
    }
}