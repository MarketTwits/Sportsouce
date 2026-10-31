package com.markettwits.sportsouce.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.Alignment
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.selfupdater.components.notification.screen.ComposableInAppNotification
import com.markettwits.sportsouce.review.root.RootReviewComponent
import com.markettwits.core_ui.items.theme.SauceMotion
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
    val rootStack by component.childStack.subscribeAsState()
    val reviewStack = (rootStack.active.instance as? RootComponent.Child.Review)?.component?.let {
        it.childStack.subscribeAsState().value
    }
    val onReview = reviewStack?.active?.instance is RootReviewComponent.Child.Review
    val onUpdate = reviewStack?.active?.instance is RootReviewComponent.Child.Notification
    val notificationState by component.updateNotification.state.collectAsState()
    var retained by remember { mutableStateOf<InAppNotificationStore.State.ShownNotification?>(null) }
    val shown = notificationState as? InAppNotificationStore.State.ShownNotification
    SideEffect { if (shown != null) retained = shown }
    component.slotChild.child?.instance?.also {
        when (it) {
            is RootComponent.Navigation.BottomBar -> {
                BottomBarScaffold(
                    component = it.component,
                    navigationVisible = !onUpdate,
                    aboveNavigation = {
                        AnimatedVisibility(
                            visible = onReview && shown != null,
                            enter = expandVertically(expandFrom = Alignment.Bottom, animationSpec = tween(280, easing = SauceMotion.feedbackEasing)) +
                                slideInVertically(tween(280, easing = SauceMotion.feedbackEasing)) { it / 3 } + fadeIn(tween(220)),
                            exit = shrinkVertically(shrinkTowards = Alignment.Bottom, animationSpec = tween(220, easing = SauceMotion.feedbackEasing)) +
                                slideOutVertically(tween(220, easing = SauceMotion.feedbackEasing)) { it / 3 } + fadeOut(tween(160)),
                        ) {
                            (shown ?: retained)?.let { notice ->
                                ComposableInAppNotification(
                                    notificationRenderer = component.updateNotificationRenderer,
                                    notificationState = notice,
                                    onNotificationOpen = { value -> component.updateNotification.obtainEvent(InAppNotificationStore.Intent.OpenNotificationScreen(value)) },
                                    onNotificationHidden = { value -> component.updateNotification.obtainEvent(InAppNotificationStore.Intent.OnNotificationHidden(value)) },
                                )
                            }
                        }
                    },
                ) {
                    NestedContent(component = component, modifier = modifier.fillMaxSize())
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
