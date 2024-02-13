package com.markettwits.starts.root.api

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.start.root.RootStartScreen
import com.markettwits.start_search.root.RootStartsSearchScreen
import com.markettwits.starts.starts.presentation.components.StartsScreen

@Composable
fun RootStartsScreen(component: RootStartsComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.configStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsComponent.Child.Start -> RootStartScreen(component = child.component)
            is RootStartsComponent.Child.Starts -> StartsScreen(component = child.component)
            is RootStartsComponent.Child.Search -> RootStartsSearchScreen(component = child.component)
        }
    }
}