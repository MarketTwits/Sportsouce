package com.markettwits.sportsouce.notification.impl.domain

import kotlinx.serialization.Serializable
import com.markettwits.sportsouce.notification.api.model.InboxNotification
import kotlin.time.Instant

@Serializable
internal data class NotificationPage(val count: Int = 0, val rows: List<InboxNotification> = emptyList())

/** Overlays acknowledged writes on pages that may have been requested before the write. */
internal data class NotificationReadState(
    val readIds: Set<Int> = emptySet(),
    val readThrough: String? = null,
    val sessionVersion: Int = 0,
) {
    fun apply(item: InboxNotification): InboxNotification {
        val through = readThrough
        val readEarlier = through != null && runCatching {
            Instant.parse(item.createdAt) <= Instant.parse(through)
        }.getOrDefault(false)
        return if (item.id in readIds || readEarlier) item.copy(isRead = true) else item
    }
}

internal class NotificationAuthRequired : Exception("Войдите в аккаунт, чтобы посмотреть уведомления")
