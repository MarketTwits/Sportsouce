package com.markettwits.sportsouce.profile.results.domain

import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

internal class ResultsPagingSource(private val repository: MyResultsRepository, private val category: String?, private val onCount: (Int) -> Unit) : OffsetAndLimitPagingSourceNew<MyResult>(20) {
    private var total: Int? = null
    override suspend fun getTotalCount() = total ?: 0
    override suspend fun load(offset: Int, limit: Int): List<MyResult> {
        if (total != null && offset >= total!!) return emptyList()
        val page = repository.feed(offset, limit, category)
        currentCoroutineContext().ensureActive()
        total = page.count
        onCount(page.count)
        return page.rows
    }
}
