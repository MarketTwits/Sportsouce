package com.markettwits.sportsouce.notification.impl.push

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences
import com.markettwits.sportsouce.notification.api.PushNotificationPreferences.State
import com.markettwits.sportsouce.notification.api.model.PushPermissionState
import com.markettwits.sportsouce.notification.impl.domain.PushPermission
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

internal class AndroidPushPermission(
    context: Context,
    private val platform: AndroidPushPlatform,
    private val display: PushNotificationDisplay,
    private val devicePreference: PushDevicePreference,
    private val push: AndroidPushNotifications,
) : PushPermission, PushNotificationPreferences {
    private val history = context.getSharedPreferences("sportsauce.notifications", Context.MODE_PRIVATE)
    private val current = MutableStateFlow(State(supported = platform.isSupported(), enabled = devicePreference.enabled))
    private val actions = MutableSharedFlow<Action>(extraBufferCapacity = 1)

    override val settings = current.asStateFlow()
    override val state = combine(settings, push.userIds) { state, userId ->
        when {
            userId == null || !state.supported -> PushPermissionState.Hidden
            !state.enabled -> PushPermissionState.DisabledInApp
            state.permission == PushPermissionState.Granted && !state.channelEnabled -> PushPermissionState.Settings
            else -> state.permission
        }
    }
    val requests = actions.asSharedFlow()

    override fun request() {
        actions.tryEmit(Action.Request)
    }

    override fun openSettings() {
        actions.tryEmit(Action.Settings)
    }

    override fun openAppSettings() {
        actions.tryEmit(Action.AppSettings)
    }

    override fun openChannelSettings() {
        actions.tryEmit(Action.ChannelSettings)
    }

    override fun enable() = setEnabled(true)

    override fun setEnabled(enabled: Boolean) {
        if (devicePreference.enabled != enabled) {
            devicePreference.enabled = enabled
            current.value = current.value.copy(enabled = enabled)
            if (!enabled) display.dismissAll()
            push.refreshDeliverySettings()
        }
        if (enabled) request()
    }

    fun update(activity: ComponentActivity): State {
        val supported = platform.isSupported()
        val permission = when {
            !supported -> PushPermissionState.Hidden
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !activity.hasRuntimePermission() ->
                deniedState(activity)
            else -> {
                if (!history.getBoolean(WAS_GRANTED, false)) {
                    history.edit().putBoolean(WAS_GRANTED, true).apply()
                }
                if (display.appNotificationsEnabled()) PushPermissionState.Granted else PushPermissionState.Settings
            }
        }
        val updated = State(supported, devicePreference.enabled, permission, display.channelEnabled())
        current.value = updated
        return updated
    }

    fun onRequestResult() {
        history.edit().putBoolean(REQUEST_ANSWERED, true).apply()
    }

    private fun deniedState(activity: ComponentActivity): PushPermissionState {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS)) {
            if (!history.getBoolean(SHOWED_RATIONALE, false)) {
                history.edit().putBoolean(SHOWED_RATIONALE, true).apply()
            }
            return PushPermissionState.Explain
        }
        return when {
            history.getBoolean(WAS_GRANTED, false) || history.getBoolean(SHOWED_RATIONALE, false) ->
                PushPermissionState.Settings
            // Dismissing the first dialog and a policy block can return the same result.
            history.getBoolean(REQUEST_ANSWERED, false) -> PushPermissionState.RetryOrSettings
            else -> PushPermissionState.Request
        }
    }

    private fun ComponentActivity.hasRuntimePermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    enum class Action { Request, Settings, AppSettings, ChannelSettings }

    private companion object {
        const val REQUEST_ANSWERED = "permission_requested"
        const val SHOWED_RATIONALE = "permission_rationale_seen"
        const val WAS_GRANTED = "permission_was_granted"
    }
}
