package com.markettwits.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.slot.child
import com.markettwits.bottom_bar.components.BottomBar
import com.markettwits.root_profile.RootProfileScreen
import com.markettwits.starts.root.RootStartsScreen

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.primary) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Children(component = component, modifier = Modifier.weight(1F))
            BottomBarInner(component = component, modifier = Modifier)
        }
    }
}

@Composable
private fun BottomBarInner(component: RootComponent, modifier: Modifier) {
    component.slotChild.child?.instance?.also {
        when (it) {
            is RootComponent.Navigation.BottomBar -> {
                BottomBar(it.component, modifier)
            }
        }
    }
}
@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Starts -> RootStartsScreen(child.component)
            is RootComponent.Child.Profile -> RootProfileScreen(child.component)
            is RootComponent.Child.Review -> RootReviewScreen(child.component)
        }
    }

}
