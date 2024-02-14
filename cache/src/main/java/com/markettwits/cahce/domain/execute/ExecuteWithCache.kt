package com.markettwits.cahce.domain.execute

import com.markettwits.cahce.domain.Cache

interface ExecuteWithCache {
    suspend fun <T> executeListWithCache(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit
    )

    suspend fun <T> executeWithCache(
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: (T) -> Unit
    )
}