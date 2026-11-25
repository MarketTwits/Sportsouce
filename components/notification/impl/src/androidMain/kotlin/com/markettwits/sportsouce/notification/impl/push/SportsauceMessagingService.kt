package com.markettwits.sportsouce.notification.impl.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.markettwits.sportsouce.notification.impl.domain.PushMessage
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class SportsauceMessagingService : FirebaseMessagingService() {
    private val push by inject<AndroidPushNotifications>()

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) {
        push.refreshRegistration()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val payload = PushMessage.parse(message.data) ?: return
        // This callback runs off the main thread. Finish local display before returning to FCM.
        runBlocking { push.receive(payload) }
    }

    override fun onDeletedMessages() {
        push.refreshInbox()
    }
}
