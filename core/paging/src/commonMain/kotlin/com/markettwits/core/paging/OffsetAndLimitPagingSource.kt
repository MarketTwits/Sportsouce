//package com.markettwits.core.paging
//
//import app.cash.paging.PagingSource
//import app.cash.paging.PagingSourceLoadParams
//import app.cash.paging.PagingSourceLoadResult
//import app.cash.paging.PagingSourceLoadResultError
//import app.cash.paging.PagingSourceLoadResultPage
//import app.cash.paging.PagingState
//
//private const val INITIAL_LOAD_SIZE = 0
//
//abstract class OffsetAndLimitPagingSource<Value : Any>(
//    private val pageSize: Int,
//) : PagingSource<Int, Value>() {
//
//    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
//        return null
//    }
//
//    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Value> {
//        return try {
//            val position = params.key ?: INITIAL_LOAD_SIZE
//            val offset = if (params.key != null) {
//           //     position * pageSize
//            } else {
//                INITIAL_LOAD_SIZE
//            }
//            val data = load(offset, params.loadSize)
//            return PagingSourceLoadResultError(Exception())
////            if (data.isEmpty()) {
////                PagingSourceLoadResultPage(emptyList())
////                PagingSourceLoadResultPage(
////                    data = data,
////                    prevKey = null,
////                    nextKey = null
////                )
//
////            } else {
////                val nextKey = position + (params.loadSize / pageSize)
////                PagingSourceLoadResultPage(
////                    data = data,
////                    prevKey = null,
////                    nextKey = nextKey
////                )
////
//////                LoadResult.Page(
//////                    data = data,
//////                    prevKey = null,
//////                    nextKey = nextKey
//////                )
////            }
//
////        } catch (exception: Throwable) {
////           // LoadResult.Error(exception)
////            PagingSourceLoadResultError(exception)
//        }
//    }
//
//    abstract suspend fun load(offset: Int, limit: Int): List<Value>
//}