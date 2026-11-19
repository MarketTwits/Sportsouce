package com.markettwits.sportsouce.start.presentation.album.domain

import app.cash.paging.*
import com.markettwits.core.paging.OffsetAndLimitPagingSourceNew
import kotlinx.coroutines.CancellationException

/** Absolute page keys and placeholders let the gallery seek without downloading every preceding page. */
internal class AlbumPhotosPagingSource(
    private val repository: StartAlbumRepository,
    private val albumId: Int,
    private val tags: List<String>,
    private val onCount: (Int) -> Unit,
) : OffsetAndLimitPagingSourceNew<AlbumPhoto>(30) {
    private var count = 0
    override val jumpingSupported: Boolean get() = true
    override suspend fun getTotalCount() = count
    override fun getRefreshKey(state: PagingState<Int, AlbumPhoto>): Int? =
        state.anchorPosition?.let { (it - state.config.initialLoadSize / 2).coerceAtLeast(0) / 30 }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, AlbumPhoto> = try {
        var offset = (params.key ?: 0).coerceAtLeast(0) * 30
        var rows = load(offset, params.loadSize)
        // A concurrent server edit may shorten the album while a jump is in flight.
        if (rows.isEmpty() && count > 0 && offset >= count) {
            offset = (count - 1) / 30 * 30
            rows = load(offset, params.loadSize)
        }
        val before = offset.coerceAtMost(count)
        val after = (count - before - rows.size).coerceAtLeast(0)
        PagingSourceLoadResultPage(
            data = rows,
            prevKey = if (before > 0) (offset / 30 - 1).coerceAtLeast(0) else null,
            nextKey = if (after > 0 && rows.isNotEmpty()) (offset + rows.size) / 30 else null,
            itemsBefore = before,
            itemsAfter = after,
        ) as PagingSourceLoadResult<Int, AlbumPhoto>
    } catch (error: CancellationException) {
        throw error
    } catch (error: Exception) {
        PagingSourceLoadResultError<Int, AlbumPhoto>(error) as PagingSourceLoadResult<Int, AlbumPhoto>
    }

    override suspend fun load(offset: Int, limit: Int): List<AlbumPhoto> {
        val page = repository.photos(albumId, offset, limit, tags)
        count = page.count.coerceAtLeast(if (page.rows.isEmpty()) 0 else offset + page.rows.size)
        onCount(page.count)
        return page.rows
    }
}
