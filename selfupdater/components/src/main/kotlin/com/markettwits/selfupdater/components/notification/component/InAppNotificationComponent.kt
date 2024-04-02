package com.markettwits.selfupdater.components.notification.component

import com.markettwits.inappnotification.api.InAppNotificationListener
import com.markettwits.selfupdater.components.notification.store.InAppNotificationStore
import kotlinx.coroutines.flow.StateFlow

interface InAppNotificationComponent : InAppNotificationListener {
    val state: StateFlow<InAppNotificationStore.State>
    fun obtainEvent(intent: InAppNotificationStore.Intent)
}