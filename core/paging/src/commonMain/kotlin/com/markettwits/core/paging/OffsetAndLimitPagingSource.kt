package com.markettwits.core.paging

import app.cash.paging.*

private const val INITIAL_LOAD_SIZE = 0

@Suppress("CAST_NEVER_SUCCEEDS")
abstract class OffsetAndLimitPagingSourceNew<Value : Any>(
    private val pageSize: Int,
) : PagingSource<Int, Value>() {

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return null
    }

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Value> {
        return try {
            val position = params.key ?: INITIAL_LOAD_SIZE
            val offset = if (params.key != null) {
                position * pageSize
            } else {
                INITIAL_LOAD_SIZE
            }
            val data = load(offset, params.loadSize)
            if (data.isEmpty()) {
                PagingSourceLoadResultPage(
                    data = data,
                    prevKey = null,
                    nextKey = null
                ) as PagingSourceLoadResult<Int, Value>
            } else {
                val nextKey = position + (params.loadSize / pageSize)
                PagingSourceLoadResultPage(
                    data = data,
                    prevKey = null,
                    nextKey = nextKey
                ) as PagingSourceLoadResult<Int, Value>
            }

        } catch (exception: Throwable) {
            PagingSourceLoadResultError<Int, Value>(
                exception
            ) as PagingSourceLoadResult<Int, Value>
        }
    }

    abstract suspend fun load(offset: Int, limit: Int): List<Value>
}