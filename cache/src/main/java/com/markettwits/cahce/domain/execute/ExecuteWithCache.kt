package com.markettwits.cahce.domain.execute

import com.markettwits.cahce.domain.Cache

interface ExecuteWithCache {
    suspend fun <T> executeListWithCache(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
        callback: (List<T>) -> Unit
    )

    suspend fun <T> executeListWithCacheApply(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>
    ): List<T>

    suspend fun <T> executeAfterApply(
        cache: Cache<List<T>>,
        launch: suspend () -> List<T>,
    )
}