package com.markettwits.cahce.execute.base

import com.markettwits.cahce.Cache
import com.markettwits.cahce.ObservableCache
import kotlinx.coroutines.flow.Flow

interface ExecuteWithCache {
    suspend fun <T> executeWithCache(
        forced: Boolean = false,
        cache: Cache<T>,
        launch: suspend () -> T,
        callback: suspend (T) -> Unit
    )

    suspend fun <T> executeWithCache(
        forced: Boolean = false,
        cache: ObservableCache<T>,
        launch: suspend () -> T,
    ): Flow<T>
}
