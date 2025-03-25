package com.markettwits.sportsouce.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.slot.child
import com.markettwits.sportsouce.bottom_bar.components.BottomBarScaffold
import com.markettwits.sportsouce.review.root.RootReviewScreen
import com.markettwits.sportsouce.root_profile.RootProfileScreen
import com.markettwits.sportsouce.starts.root.RootStartsScreen

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {

    component.slotChild.child?.instance?.also {
        when (it) {
            is RootComponent.Navigation.BottomBar -> {
                BottomBarScaffold(component = it.component, modifier = modifier) {
                    NestedContent(
                        component = component, modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    )
                }
            }
        }
    }
}

@Composable
private fun NestedContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
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
