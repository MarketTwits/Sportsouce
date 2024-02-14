package com.markettwits.cahce.domain.execute.list

import com.markettwits.cahce.domain.Cache

interface ExecuteListWithCache {
    suspend fun <T> executeListWithCache(
        forced: Boolean = false,
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit
    )
}