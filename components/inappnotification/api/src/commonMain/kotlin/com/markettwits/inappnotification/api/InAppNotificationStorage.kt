package com.markettwits.inappnotification.api

import com.markettwits.inappnotification.api.model.InAppNotification

interface InAppNotificationStorage {
    fun subscribe(listener: InAppNotificationListener)
    fun unsubscribe()
    fun addNotification(notification: InAppNotification)
}
