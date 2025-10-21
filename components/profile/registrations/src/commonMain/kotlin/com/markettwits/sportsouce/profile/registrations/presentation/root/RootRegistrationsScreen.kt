package com.markettwits.sportsouce.profile.registrations.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.profile.registrations.presentation.detail.screen.StartOrderStartScreen
import com.markettwits.sportsouce.profile.registrations.presentation.list.screen.MyRegistrationsScreen
import com.markettwits.sportsouce.start.root.RootStartScreen

@OptIn(FaultyDecomposeApi::class)
@Composable
fun RootRegistrationsScreen(component: RootRegistrationsComponent) {

    val childStack by component.childStack.subscribeAsState()

    /**
     * Previous StackAnimation
     * Children(
     *         stack = childStack,
     *         animation = stackAnimation { from, to, direction ->
     *             if (direction.isFront) {
     *                 slide() + fade()
     *             } else {
     *                 scale(frontFactor = 1F, backFactor = 0.7F) + fade()
     *             }
     *         },
     *     )
     */
    Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootRegistrationsComponent.ChildStack.Registrations ->
                MyRegistrationsScreen(component = child.component)

            is RootRegistrationsComponent.ChildStack.Start ->
                RootStartScreen(component = child.component)

            is RootRegistrationsComponent.ChildStack.Registration ->
                StartOrderStartScreen(component = child.component)
        }
    }
}

