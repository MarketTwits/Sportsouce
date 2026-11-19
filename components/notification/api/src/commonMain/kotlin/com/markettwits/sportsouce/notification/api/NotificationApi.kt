package com.markettwits.sportsouce.notification.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

/** One inbox and unread counter shared by all application tabs. */
interface NotificationApi {
    val unreadCount: StateFlow<Int>
    suspend fun refreshUnreadCount()
    /** Immediately discard the previous account's notification state on sign-out. */
    fun clear()
    fun createComponent(
        context: ComponentContext,
        onBack: () -> Unit,
        onOpenStart: (Int) -> Unit,
    ): NotificationComponent
}
