package com.markettwits.sportsouce.notification.impl.push

import com.markettwits.sportsouce.auth.service.api.AuthSession
import com.markettwits.sportsouce.notification.impl.domain.PushMessage
import com.markettwits.sportsouce.notification.impl.domain.PushRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

internal class AndroidPushNotifications(
    private val registration: PushRegistration,
    private val display: PushNotificationDisplay,
) {
    private val changes = Channel<Unit>(Channel.CONFLATED)
    private var deliveryAllowed = display.canShow()
    val inboxUpdates = changes.receiveAsFlow()
    val userIds = registration.userIds

    fun refreshRegistration() = registration.refresh()

    fun refreshDeliverySettings() {
        val allowed = display.canShow()
        if (allowed != deliveryAllowed) {
            deliveryAllowed = allowed
            registration.deliverySettingsChanged()
        } else {
            registration.refresh()
        }
    }

    fun refreshInbox() {
        changes.trySend(Unit)
    }

    suspend fun receive(message: PushMessage) = withTimeoutOrNull(2_000) {
        withContext(Dispatchers.Main.immediate) {
            if (isCurrentAccount(message.recipientUserId)) {
                display.show(message)
                refreshInbox()
            }
        }
    }

    suspend fun isCurrentAccount(userId: Int): Boolean = registration.isCurrentAccount(userId)

    suspend fun beforeSignOut(session: AuthSession) = withContext(Dispatchers.Main.immediate) {
        registration.beforeSignOut(session)
    }
}
