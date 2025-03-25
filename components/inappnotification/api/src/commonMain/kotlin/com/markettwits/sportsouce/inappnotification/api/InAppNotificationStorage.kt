package com.markettwits.sportsouce.inappnotification.api

import com.markettwits.sportsouce.inappnotification.api.model.InAppNotification

interface InAppNotificationStorage {
    fun subscribe(listener: InAppNotificationListener)
    fun unsubscribe()
    fun addNotification(notification: InAppNotification)
}
