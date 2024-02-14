package com.markettwits.cahce.domain.execute.base

import com.markettwits.cahce.domain.Cache

interface ExecuteWithCache {
    suspend fun <T> executeWithCache(
        forced: Boolean = false,
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: (T) -> Unit
    )
}
