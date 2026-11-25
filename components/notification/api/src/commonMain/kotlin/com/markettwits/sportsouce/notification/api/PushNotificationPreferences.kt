package com.markettwits.sportsouce.notification.api

import com.markettwits.sportsouce.notification.api.model.PushPermissionState
import kotlinx.coroutines.flow.StateFlow

/** Registered by platforms that support configuring push notifications on this device. */
interface PushNotificationPreferences {
    val settings: StateFlow<State>
    fun setEnabled(enabled: Boolean)
    fun request()
    fun openAppSettings()
    fun openChannelSettings()

    data class State(
        val supported: Boolean,
        val enabled: Boolean,
        val permission: PushPermissionState = PushPermissionState.Hidden,
        val channelEnabled: Boolean = true,
    )
}
