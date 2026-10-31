package com.markettwits.sportsouce.inappnotification.impl.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.impl.composable.type.ComposableInAppNotificationSelfUpdateReady
import com.markettwits.sportsouce.inappnotification.impl.composable.type.ComposableInAppNotificationUpdateStatus

@Composable
fun ComposableInAppNotification(
    notification: InAppNotification,
    onNotificationHidden: () -> Unit,
    onNotificationOpen: (notification: InAppNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    ComposableInAppNotificationCard(
        notification,
        onNotificationHidden,
        onNotificationOpen,
        modifier
    )
}

@Composable
@Suppress("LongMethod")
private fun ComposableInAppNotificationCard(
    notification: InAppNotification,
    onNotificationHidden: () -> Unit,
    onNotificationOpen: (notification: InAppNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        when (notification) {
            is InAppNotification.SelfUpdateReady -> {
                ComposableInAppNotificationSelfUpdateReady(
                    notification = notification,
                    onClickAction = { onNotificationOpen(notification) },
                    onDismiss = onNotificationHidden,
                )
            }

            is InAppNotification.SelfUpdateError -> {
                ComposableInAppNotificationUpdateStatus(isError = true, onDismiss = onNotificationHidden, failureKind = notification.kind)
            }

            is InAppNotification.SelfUpdateProgress -> {
                ComposableInAppNotificationUpdateStatus(isError = false, onDismiss = onNotificationHidden, percent = notification.percent, paused = notification.paused, onOpen = { onNotificationOpen(notification) })
            }

            is InAppNotification.SelfUpdateStarted -> {
                ComposableInAppNotificationUpdateStatus(isError = false, onDismiss = onNotificationHidden, onOpen = { onNotificationOpen(notification) })
            }

            is InAppNotification.SelfUpdateCleared -> Unit

            is InAppNotification.HiddenApp -> {

            }

            is InAppNotification.Error -> {

            }
        }
    }
}
