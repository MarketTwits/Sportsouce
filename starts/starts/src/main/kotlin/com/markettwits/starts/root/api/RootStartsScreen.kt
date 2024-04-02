package com.markettwits.starts.root.api

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.slot.child
import com.markettwits.selfupdater.components.selft_update.screen.SelfUpdateScreen
import com.markettwits.selfupdater.components.notification.screen.NotificationScreen
import com.markettwits.start.root.RootStartScreen
import com.markettwits.start_search.root.RootStartsSearchScreen
import com.markettwits.starts.starts.presentation.screen.StartsScreen

@Composable
fun RootStartsScreen(component: RootStartsComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.configStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootStartsComponent.Child.Start -> RootStartScreen(component = child.component)
            is RootStartsComponent.Child.Starts -> StartsScreen(
                component = child.component,
                notification = {
                    component.childSlot.child?.instance?.also { child ->
                        when (child) {
                            is RootStartsComponent.ChildSlot.Notification -> NotificationScreen(
                                modifier = it,
                                notificationComponent = child.component,
                                renderer = child.render
                            )
                        }
                    }
                },
                showNotification = component.childSlot.child?.instance != null
            )

            is RootStartsComponent.Child.Search -> RootStartsSearchScreen(component = child.component)
            is RootStartsComponent.Child.Notification -> SelfUpdateScreen(component = child.component)
        }
    }

}
