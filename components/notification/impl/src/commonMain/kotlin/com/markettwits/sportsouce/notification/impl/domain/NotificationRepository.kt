package com.markettwits.sportsouce.notification.impl.domain

import com.markettwits.sportsouce.notification.api.model.InboxNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface NotificationRepository {
    val unreadCount: StateFlow<Int>
    val readState: StateFlow<NotificationReadState>
    val inboxUpdates: Flow<Unit>
    fun invalidateInbox()
    suspend fun page(offset: Int, limit: Int): NotificationPage
    suspend fun refreshUnreadCount()
    suspend fun read(item: InboxNotification)
    suspend fun readAll()
    fun clear()
}
