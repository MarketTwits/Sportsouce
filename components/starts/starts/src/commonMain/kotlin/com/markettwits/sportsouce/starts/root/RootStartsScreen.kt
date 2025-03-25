package com.markettwits.sportsouce.starts.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.sportsouce.settings.root.RootSettingsScreen
import com.markettwits.sportsouce.start.root.RootStartScreen
import com.markettwits.sportsouce.start.search.root.RootStartsSearchScreen
import com.markettwits.sportsouce.starts.starts.presentation.screen.StartsScreen

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
            is RootStartsComponent.Child.Settings -> RootSettingsScreen(component = child.component)
        }
    }
}
