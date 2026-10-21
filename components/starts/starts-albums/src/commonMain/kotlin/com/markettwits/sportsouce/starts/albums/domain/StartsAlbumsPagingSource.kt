package com.markettwits.sportsouce.starts.albums.domain

import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew

internal class StartsAlbumsPagingSource(
    private val repository: StartsAlbumsRepository,
    private val filterText: String,
    private val year: Int?,
    pageSize: Int,
) : OffsetAndLimitPagingSourceNew<StartAlbumsItem>(pageSize) {
    private var totalCount = 0
    override suspend fun getTotalCount() = totalCount
    override suspend fun load(offset: Int, limit: Int): List<StartAlbumsItem> {
        val page = repository.albums(offset, limit, filterText, year)
        totalCount = page.count
        // Keep raw page boundaries. PagingData filters empty groups after pagination, so
        // a page consisting only of empty albums cannot hide later pages with photos.
        return page.rows
    }
}
