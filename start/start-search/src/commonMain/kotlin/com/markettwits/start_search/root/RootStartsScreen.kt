package com.markettwits.start_search.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.start.root.RootStartScreen
import com.markettwits.start_filter.root.RootStartFilterScreen
import com.markettwits.start_search.search.presentation.components.inner.StartsSearchScreen

@Composable
fun RootStartsSearchScreen(component: RootStartsSearchComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsSearchComponent.Child.Search -> StartsSearchScreen(component = child.component)
            is RootStartsSearchComponent.Child.Start -> RootStartScreen(child.component)
            is RootStartsSearchComponent.Child.Filter -> RootStartFilterScreen(component = child.component)
        }
    }
}