package com.markettwits.sportsouce.notification.impl.domain

import com.markettwits.sportsouce.notification.api.model.InboxNotification
import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

internal class NotificationPagingSource(
    private val repository: NotificationRepository,
    private val onRefreshFinished: () -> Unit = {},
) : OffsetAndLimitPagingSourceNew<InboxNotification>(20) {
    private var total: Int? = null
    override suspend fun getTotalCount() = total ?: 0
    override suspend fun load(offset: Int, limit: Int): List<InboxNotification> {
        if (total?.let { offset >= it } == true) return emptyList()
        try {
            val page = repository.page(offset, limit)
            currentCoroutineContext().ensureActive()
            total = page.count
            return page.rows
        } finally {
            if (offset == 0) onRefreshFinished()
        }
    }
}
