package com.markettwits.cahce.execute.list

import com.markettwits.cahce.Cache

interface ExecuteListWithCache {
    suspend fun <T> executeListWithCache(
        forced: Boolean = false,
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: suspend (List<T>) -> Unit
    )

    suspend fun <T> executeListWithCache(
        forced: Boolean = false,
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
    ): List<T>
}