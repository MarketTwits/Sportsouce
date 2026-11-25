package com.markettwits.sportsouce.notification.impl.push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.markettwits.notification.impl.R
import com.markettwits.sportsouce.notification.impl.domain.PushMessage

internal class PushNotificationDisplay(
    private val context: Context,
    private val devicePreference: PushDevicePreference,
) {
    private val manager = context.getSystemService(NotificationManager::class.java)
    private val notifications = NotificationManagerCompat.from(context)

    init {
        manager.createNotificationChannel(NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.push_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ))
    }

    fun appNotificationsEnabled(): Boolean = notifications.areNotificationsEnabled()

    fun channelEnabled(): Boolean =
        manager.getNotificationChannel(CHANNEL_ID)?.importance != NotificationManager.IMPORTANCE_NONE

    fun canShow(): Boolean = devicePreference.enabled && appNotificationsEnabled() && channelEnabled()

    fun show(message: PushMessage) {
        if (!canShow()) return
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName) ?: return
        val intent = launchIntent
            .setAction(OPEN_INBOX)
            .putExtra(RECIPIENT_ID, message.recipientUserId)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, message.deliveryId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_start_notifications)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVisibility(Notification.VISIBILITY_PRIVATE)
            .build()
        // Permission can change between the check and the notify call.
        try {
            manager.notify(CHANNEL_ID, message.deliveryId, notification)
        } catch (_: SecurityException) {
            // Android revoked notification permission while building the notification.
        }
    }

    fun dismissAll() {
        manager.activeNotifications.filter { it.tag == CHANNEL_ID }.forEach {
            manager.cancel(it.tag, it.id)
        }
    }

    companion object {
        const val CHANNEL_ID = "sportsauce_account_notifications"
        private const val OPEN_INBOX = "com.markettwits.sportsouce.action.OPEN_NOTIFICATION_INBOX"
        private const val RECIPIENT_ID = "recipient_user_id"

        fun takeRecipient(intent: Intent): Int? {
            if (intent.action != OPEN_INBOX) return null
            val userId = intent.getIntExtra(RECIPIENT_ID, 0)
            intent.removeExtra(RECIPIENT_ID)
            return userId.takeIf { it > 0 }
        }
    }
}
