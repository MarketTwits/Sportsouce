package com.markettwits.inappnotification.api

import com.markettwits.inappnotification.api.model.InAppNotification

@FunctionalInterface
interface InAppNotificationListener {
    suspend fun onNewNotification(notification: InAppNotification)
}
