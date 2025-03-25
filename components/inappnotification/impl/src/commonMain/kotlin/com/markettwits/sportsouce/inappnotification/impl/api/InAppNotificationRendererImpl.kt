package com.markettwits.sportsouce.inappnotification.impl.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationRenderer
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification
import com.markettwits.sportsouce.inappnotification.impl.composable.ComposableInAppNotification

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
