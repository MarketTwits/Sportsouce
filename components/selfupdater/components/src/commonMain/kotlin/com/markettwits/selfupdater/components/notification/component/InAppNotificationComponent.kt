package com.markettwits.selfupdater.components.notification.component

import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationListener
import kotlinx.coroutines.flow.StateFlow

interface InAppNotificationComponent : InAppNotificationListener {
    val state: StateFlow<InAppNotificationStore.State>
    fun obtainEvent(intent: InAppNotificationStore.Intent)
}