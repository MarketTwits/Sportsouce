package com.markettwits.sportsouce.inappnotification.api

import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

interface InAppNotificationListener {
    suspend fun onNewNotification(notification: InAppNotification)
}
