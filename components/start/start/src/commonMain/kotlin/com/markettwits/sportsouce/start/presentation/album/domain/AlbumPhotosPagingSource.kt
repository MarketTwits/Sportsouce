package com.markettwits.sportsouce.start.presentation.album.domain

import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew

internal class AlbumPhotosPagingSource(
    private val repository: StartAlbumRepository,
    private val albumId: Int,
    private val tags: List<String>,
    private val onCount: (Int) -> Unit,
) : OffsetAndLimitPagingSourceNew<AlbumPhoto>(30) {
    private var count = 0
    override suspend fun getTotalCount() = count
    override suspend fun load(offset: Int, limit: Int): List<AlbumPhoto> {
        val page = repository.photos(albumId, offset, limit, tags)
        count = page.count
        onCount(page.count)
        return page.rows
    }
}
