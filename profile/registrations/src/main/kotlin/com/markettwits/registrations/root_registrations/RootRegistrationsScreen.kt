package com.markettwits.registrations.root_registrations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.isFront
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.registrations.registrations_list.presentation.MyRegistrationsScreen
import com.markettwits.start.root.RootStartScreen

@OptIn(FaultyDecomposeApi::class)
@Composable
fun RootRegistrationsScreen(component: RootRegistrationsComponent) {

    val childStack by component.childStack.subscribeAsState()

    Children(
        stack = childStack,
        animation = stackAnimation { from, to, direction ->
            if (direction.isFront) {
                slide() + fade()
            } else {
                scale(frontFactor = 1F, backFactor = 0.7F) + fade()
            }
        },
    ) {
        when (val child = it.instance) {
            is RootRegistrationsComponent.ChildStack.Registrations ->
                MyRegistrationsScreen(component = child.component)

            is RootRegistrationsComponent.ChildStack.Start ->
                RootStartScreen(component = child.component)
        }
    }
}

