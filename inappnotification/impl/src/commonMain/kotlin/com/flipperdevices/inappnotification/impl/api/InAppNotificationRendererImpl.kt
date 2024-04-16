package com.flipperdevices.inappnotification.impl.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flipperdevices.inappnotification.impl.composable.ComposableInAppNotification
import com.markettwits.inappnotification.api.InAppNotificationRenderer
import com.markettwits.inappnotification.api.model.InAppNotification

class InAppNotificationRendererImpl : InAppNotificationRenderer {
    @Composable
    override fun InAppNotification(
        notification: InAppNotification,
        modifier: Modifier,
        onNotificationHidden: () -> Unit,
        onNotificationOpen: (notification: InAppNotification) -> Unit
    ) {
        ComposableInAppNotification(
            notification,
            onNotificationHidden,
            onNotificationOpen,
            modifier
        )
    }
}
