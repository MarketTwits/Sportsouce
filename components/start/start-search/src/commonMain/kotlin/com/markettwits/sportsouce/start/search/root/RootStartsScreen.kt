package com.markettwits.sportsouce.start.search.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.sportsouce.start.root.RootStartScreen
import com.markettwits.sportsouce.start.search.filter.presentation.screen.StartFilterScreen
import com.markettwits.sportsouce.start.search.search.presentation.components.inner.StartsSearchScreen

@Composable
fun RootStartsSearchScreen(component: RootStartsSearchComponent) {

    val childStack by component.childStack.subscribeAsState()
    val childSlot by component.childSlot.subscribeAsState()

    childSlot.child?.instance?.also { child ->
        when (child) {
            is RootStartsSearchComponent.ChildSlot.Filter -> StartFilterScreen(component = child.component)
        }
    }

    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsSearchComponent.ChildStack.Search -> StartsSearchScreen(component = child.component)
            is RootStartsSearchComponent.ChildStack.Start -> RootStartScreen(child.component)
            // is RootStartsSearchComponent.ChildStack.Filter -> RootStartFilterScreen(component = child.component)
        }
    }
}