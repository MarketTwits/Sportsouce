package com.markettwits.sportsouce.root

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime
    val imePadding = imeInsets.asPaddingValues(density)
    val keyboardHeight = imeInsets.getBottom(density)
    val isKeyboardVisible = keyboardHeight > 0

    component.slotChild.child?.instance?.also {
        when (it) {
            is RootComponent.Navigation.BottomBar -> {
                BottomBarScaffold(component = it.component, modifier = modifier) {
                    NestedContent(
                        component = component,
                        modifier = Modifier
                            .padding(WindowInsets.statusBars.asPaddingValues())
                            .padding(
                                bottom = if (isKeyboardVisible) {
                                    imePadding.calculateBottomPadding() / 2 + 20.dp
                                } else {
                                    0.dp
                                }
                            )
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun NestedContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        modifier = modifier,
        stack = component.childStack,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Starts -> RootStartsScreen(child.component)
            is RootComponent.Child.Profile -> RootProfileScreen(child.component)
            is RootComponent.Child.Review -> RootReviewScreen(child.component)
        }
    }
}
