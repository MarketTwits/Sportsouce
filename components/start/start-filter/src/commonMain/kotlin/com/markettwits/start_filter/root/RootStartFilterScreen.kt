package com.markettwits.start_filter.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.start.root.RootStartScreen
import com.markettwits.start_filter.start_filter.presentation.screen.StartFilterScreen
import com.markettwits.start_filter.starts.StartsFilteredScreen

@Composable
fun RootStartFilterScreen(component: RootStartFilterComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartFilterComponent.Child.Filter -> StartFilterScreen(component = child.component)
            is RootStartFilterComponent.Child.Starts -> StartsFilteredScreen(child.component)
            is RootStartFilterComponent.Child.Start -> RootStartScreen(component = child.component)
        }
    }
}