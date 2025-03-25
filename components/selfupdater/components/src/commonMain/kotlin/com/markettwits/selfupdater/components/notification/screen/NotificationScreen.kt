package com.markettwits.selfupdater.components.notification.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.selfupdater.components.notification.component.InAppNotificationComponent
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationRenderer
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    notificationComponent: InAppNotificationComponent,
    renderer: InAppNotificationRenderer,
) {
    val state by notificationComponent.state.collectAsState()
    ComposableInAppNotification(
        modifier = modifier,
        notificationRenderer = renderer,
        notificationState = state,
        onNotificationOpen = {
            notificationComponent.obtainEvent(
                InAppNotificationStore.Intent.OpenNotificationScreen(
                    it
                )
            )
        },
        onNotificationHidden = {
            notificationComponent.obtainEvent(
                InAppNotificationStore.Intent.OnNotificationHidden(
                    it
                )
            )
        }
    )
}

@Composable
fun ComposableInAppNotification(
    notificationRenderer: InAppNotificationRenderer,
    notificationState: InAppNotificationStore.State,
    onNotificationHidden: (notification: InAppNotification) -> Unit,
    onNotificationOpen: (notification: InAppNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    if (notificationState !is InAppNotificationStore.State.ShownNotification) {
        return
    } else {
        notificationRenderer.InAppNotification(
            notification = notificationState.notification,
            onNotificationOpen = onNotificationOpen,
            onNotificationHidden = {
                onNotificationHidden(notificationState.notification)
            },
            modifier = modifier
        )
    }
}