package com.markettwits.sportsouce.notification.impl.api

import com.arkivanov.decompose.ComponentContext
import com.markettwits.sportsouce.notification.api.NotificationApi
import com.markettwits.sportsouce.notification.impl.domain.NotificationRepository
import com.markettwits.sportsouce.notification.impl.presentation.component.NotificationComponentBase
import com.markettwits.sportsouce.notification.impl.presentation.store.NotificationStoreFactory
import kotlinx.coroutines.CancellationException

internal class NotificationApiImpl(
    private val repository: NotificationRepository,
    private val factory: NotificationStoreFactory,
) : NotificationApi {
    override val unreadCount = repository.unreadCount
    override fun clear() = repository.clear()
    override suspend fun refreshUnreadCount() {
        try { repository.refreshUnreadCount() }
        catch (cancelled: CancellationException) { throw cancelled }
        catch (_: Exception) { /* Keep the last count during a temporary connection failure. */ }
    }
    override fun createComponent(context: ComponentContext, onBack: () -> Unit, onOpenStart: (Int) -> Unit) =
        NotificationComponentBase(context, factory, onBack, onOpenStart)
}
