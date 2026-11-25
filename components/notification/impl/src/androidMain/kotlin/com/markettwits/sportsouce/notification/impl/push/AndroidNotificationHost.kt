package com.markettwits.sportsouce.notification.impl.push

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.markettwits.sportsouce.notification.api.model.PushPermissionState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/** Connects a platform activity to the notification feature and its system permission. */
class AndroidNotificationHost(
    private val activity: ComponentActivity,
    private val onOpenInbox: () -> Unit,
    private val onInboxChanged: () -> Unit,
) {
    private val push by activity.inject<AndroidPushNotifications>()
    private val permission by activity.inject<AndroidPushPermission>()
    private var permissionRequestInProgress = false
    private val requestPermission = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        permissionRequestInProgress = false
        permission.onRequestResult()
        refreshPermission()
    }

    init {
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                refreshPermission()
                launch { permission.requests.collect(::handlePermissionAction) }
                launch { push.inboxUpdates.collect { onInboxChanged() } }
            }
        }
    }

    fun handleIntent(intent: Intent) {
        val user = PushNotificationDisplay.takeRecipient(intent) ?: return
        activity.lifecycleScope.launch {
            if (push.isCurrentAccount(user)) onOpenInbox()
        }
    }

    private fun refreshPermission() {
        permission.update(activity)
        push.refreshDeliverySettings()
    }

    private fun handlePermissionAction(action: AndroidPushPermission.Action) {
        val state = permission.update(activity)
        when (action) {
            AndroidPushPermission.Action.AppSettings -> openSettings(channel = false)
            AndroidPushPermission.Action.ChannelSettings -> openSettings(channel = true)
            AndroidPushPermission.Action.Settings -> openSettings(
                channel = state.permission == PushPermissionState.Granted && !state.channelEnabled,
            )
            AndroidPushPermission.Action.Request -> when (state.permission) {
                PushPermissionState.Settings -> openSettings(channel = false)
                PushPermissionState.Granted -> if (!state.channelEnabled) openSettings(channel = true)
                PushPermissionState.Request, PushPermissionState.Explain, PushPermissionState.RetryOrSettings -> {
                    if (!permissionRequestInProgress) {
                        permissionRequestInProgress = true
                        requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun openSettings(channel: Boolean) {
        val packageName = activity.packageName
        val appSettings = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        if (channel && tryOpenSettings(
                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, PushNotificationDisplay.CHANNEL_ID),
            )
        ) return
        if (tryOpenSettings(appSettings)) return
        tryOpenSettings(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))
    }

    private fun tryOpenSettings(intent: Intent): Boolean = try {
        activity.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }
}
