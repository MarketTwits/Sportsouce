package com.markettwits.sportsouce.notification.api

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.auth.flow.api.root.RootAuthFlowComponent
import com.markettwits.sportsouce.notification.api.store.NotificationStore
import kotlinx.coroutines.flow.StateFlow

interface NotificationComponent {
    val state: StateFlow<NotificationStore.State>
    val authSlot: Value<ChildSlot<*, RootAuthFlowComponent>>
    fun obtainEvent(intent: NotificationStore.Intent)
}
