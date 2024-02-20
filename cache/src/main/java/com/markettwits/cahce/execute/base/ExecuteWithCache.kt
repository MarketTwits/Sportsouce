package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache

interface ExecuteWithCache {
    suspend fun <T> executeWithCache(
        forced: Boolean = false,
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: (T) -> Unit
    )
}
