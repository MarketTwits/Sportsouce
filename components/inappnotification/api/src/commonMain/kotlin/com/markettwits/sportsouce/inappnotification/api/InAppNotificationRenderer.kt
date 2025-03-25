package com.markettwits.sportsouce.inappnotification.api


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

interface InAppNotificationRenderer {
    @Composable
    fun InAppNotification(
        notification: InAppNotification,
        modifier: Modifier,
        onNotificationHidden: () -> Unit,
        onNotificationOpen: (notification: InAppNotification) -> Unit,
    )
}
