package com.markettwits.shop.paging

private const val INITIAL_LOAD_SIZE = 0

abstract class OffsetAndLimitPagingSource<Value : Any>(
    private val pageSize: Int,
) : androidx.paging.PagingSource<Int, Value>() {

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, Value>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        return try {
            val position = params.key ?: INITIAL_LOAD_SIZE
            val offset = if (params.key != null) {
                position * pageSize
            } else {
                INITIAL_LOAD_SIZE
            }
            val data = load(offset, params.loadSize)
            if (data.isEmpty()) {
                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = null
                )
            } else {
                val nextKey = position + (params.loadSize / pageSize)
                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = nextKey
                )
            }

        } catch (exception: Throwable) {
            LoadResult.Error(exception)
        }
    }

    abstract suspend fun load(offset: Int, limit: Int): List<Value>
}